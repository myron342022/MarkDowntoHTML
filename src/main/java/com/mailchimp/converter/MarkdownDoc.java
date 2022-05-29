package com.mailchimp.converter;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Value;

/**
 * Represents a block of mark down text that might contain different parts.
 */
@Builder
@Value
public class MarkdownDoc extends MarkdownComponent {

  List<MarkdownComponent> components;

  public static MarkdownDoc parse(String rawText) {

    String[] rawComponents = rawText.split("\\n\\n");

    List<MarkdownComponent> components = new ArrayList<>();
    for (String rawComponent : rawComponents) {

      Optional<Heading> heading = Heading.parse(rawComponent);

      if (heading.isPresent()) {
        components.add(heading.get());
      } else {
        Paragraph.parse(rawComponent).ifPresent(components::add);
      }

    }
    return MarkdownDoc.builder().components(components).build();
  }

  @Override
  void renderAsHtml(StringBuilder sb) {
    for (int i = 0; i < components.size(); i++) {
      components.get(i).renderAsHtml(sb);
      if(i != components.size() - 1) {
        sb.append("\n\n");
      }
    }
  }
}
