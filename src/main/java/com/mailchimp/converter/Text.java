package com.mailchimp.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Value;

/**
 * A raw text block, which can be present in a Heading or a Paragraph
 */
@Value
@Builder
public class Text extends MarkdownComponent {

  /**
   * A text can be composed of plain text (words) or specially formatted
   * Links
   */
  abstract static class TextComponent extends MarkdownComponent {}

  @Value
  @Builder
  public static class Words extends TextComponent {

    String words;

    @Override
    void renderAsHtml(StringBuilder sb) {
      sb.append(words);
    }
  }

  @Value
  @Builder
  public static class Link extends TextComponent {

    String text;
    String reference;

    @Override
    void renderAsHtml(StringBuilder sb) {
      sb.append("<a href=\"");
      sb.append(reference);
      sb.append("\">");
      sb.append(text);
      sb.append("</a>");
    }
  }

  List<TextComponent> textComponents;

  public static Text empty = Text.builder().textComponents(Collections.emptyList()).build();

  private static String coreLinkPattern = "\\[(.*)\\]\\((.*)\\)"; // This matches a simple markdown link

  private static Pattern linkBasedSplitterPattern() {
    // A pattern to be used in string splitter that keeps the delimiter (link) as part of the output
    // using look ahead.
    // Pattern.compile ensures that this pattern is valid
    return Pattern.compile(String.format("(?=(%s))", coreLinkPattern));
  }

  private static Pattern linkExtractorPattern() {
    return Pattern.compile(String.format("%s(.*)", coreLinkPattern), Pattern.DOTALL);
  }


  private static final String patternForSplitting = linkBasedSplitterPattern().pattern();
  private static final Pattern patternForExtractingLinks = linkExtractorPattern(); // Compile the pattern once


  private static List<TextComponent> splitLinkAndText(String textStartingWithLink) {
    Matcher m = patternForExtractingLinks.matcher(textStartingWithLink);

    List<TextComponent> result = new ArrayList<>(2);
    if (m.find()) {

      result.add(
          Link.builder()
              .text(m.group(1))
              .reference(m.group(2))
              .build()
      );

      if (m.group(3).length() > 0) {
        // If the line had just one link and no text, then we should not add this.
        result.add(
            Words.builder().words(m.group(3)).build()
        );
      }

      return result;
    }
    return Collections.singletonList(Words.builder().words(textStartingWithLink).build());
  }

  public static Optional<Text> parse(String rawText) {
    String[] splitted = rawText.split(patternForSplitting);

    List<TextComponent> componentsInParagraph = new ArrayList<>();
    for (String s : splitted) {
      componentsInParagraph.addAll(splitLinkAndText(s));
    }

    return Optional.of(Text.builder().textComponents(componentsInParagraph).build());
  }


  @Override
  void renderAsHtml(StringBuilder sb) {
    textComponents.forEach(text -> text.renderAsHtml(sb));
  }
}
