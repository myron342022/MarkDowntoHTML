package com.mailchimp.converter;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Heading extends MarkdownComponent {

  private final Text text;
  private final int level;

  public static Optional<Heading> parse(String rawText) {

    if (rawText.startsWith("#")) {

      // drop #
      int headerLevel = 0;
      int startingIndexOfText = 0;
      for (int i = 0; i < rawText.length(); i++) {
        if (rawText.charAt(i) == '#') {
          headerLevel++;
          startingIndexOfText ++;
        } else if(rawText.charAt(i) == ' '){
          startingIndexOfText++;
        } else {
          break;
        }
      }

      String remaining = rawText.substring(startingIndexOfText);
      Heading heading = Heading.builder()
          .level(headerLevel)
          .text(Text.parse(remaining).orElseGet(() -> Text.empty))
          .build();
      return Optional.of(heading);
    } else {
      return Optional.empty();
    }
  }

  @Override
  void renderAsHtml(StringBuilder sb) {
    sb.append("<h");
    sb.append(level);
    sb.append(">");
    text.renderAsHtml(sb);
    sb.append("</h");
    sb.append(level);
    sb.append(">");
  }
}
