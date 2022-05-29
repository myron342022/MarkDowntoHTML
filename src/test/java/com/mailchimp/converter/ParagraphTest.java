package com.mailchimp.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import com.mailchimp.converter.Text.Link;
import com.mailchimp.converter.Text.Words;
import java.util.Optional;
import org.junit.Test;

public class ParagraphTest {
  @Test
  public void testSimpleParagraphToHtml() {
    String rawMd = "This is a paragraph";

    Optional<Paragraph> actual = Paragraph.parse(rawMd);

    assertThat(actual.isPresent(), is(true));
    StringBuilder sb = new StringBuilder();
    actual.get().renderAsHtml(sb);
    assertThat(sb.toString(), is(
        "<p>This is a paragraph</p>"
    ));
  }

  @Test
  public void testParagraphWithMultipleLinks() {
    String rawMd = "This is a paragraph [with an inline link](http://google.com). Neat, eh? [with an inline link](http://google.com)";

    Optional<Paragraph> actual = Paragraph.parse(rawMd);
    assertThat(actual.isPresent(), is(true));

    StringBuilder sb = new StringBuilder();
    actual.get().renderAsHtml(sb);
    assertThat(sb.toString(), is(
  "<p>This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh? <a href=\"http://google.com\">with an inline link</a></p>"
    ));
  }
}
