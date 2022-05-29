package com.mailchimp.converter;

import java.util.Optional;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Paragraph extends MarkdownComponent {
  Text text;

  public static Optional<Paragraph> parse(String rawString) {
    return Text.parse(rawString).map(t -> Paragraph.builder().text(t).build());
  }

  @Override
  void renderAsHtml(StringBuilder sb) {
    sb.append("<p>");
    text.renderAsHtml(sb);
    sb.append("</p>");
  }
}
