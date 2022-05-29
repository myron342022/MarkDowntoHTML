package com.mailchimp.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class MarkdownDocTest {

  private String convert(String rawText) {
    StringBuilder htmlBuilder = new StringBuilder();
    MarkdownDoc.parse(rawText).renderAsHtml(htmlBuilder);

    return htmlBuilder.toString();
  }

  @Test
  public void testSimpleDocument() {

    String doc = "# Sample Document\n"
        + "\n"
        + "Hello!\n"
        + "\n"
        + "This is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.";
    assertThat(
        convert(doc), is(
            "<h1>Sample Document</h1>\n"
                + "\n"
                + "<p>Hello!</p>\n"
                + "\n"
                + "<p>This is sample markdown for the <a href=\"https://www.mailchimp.com\">Mailchimp</a> homework assignment.</p>"
        )
    );
  }

  @Test
  public void testSample2() {
    String doc = "# Header one\n"
        + "\n"
        + "Hello there\n"
        + "\n"
        + "How are you?\n"
        + "What's going on?\n"
        + "\n"
        + "## Another Header\n"
        + "\n"
        + "This is a paragraph [with an inline link](http://google.com). Neat, eh?\n"
        + "\n"
        + "## This is a header [with a link](http://yahoo.com)";

    assertThat(
        convert(doc), is(
            "<h1>Header one</h1>\n"
                + "\n"
                + "<p>Hello there</p>\n"
                + "\n"
                + "<p>How are you?\n"
                + "What's going on?</p>\n"
                + "\n"
                + "<h2>Another Header</h2>\n"
                + "\n"
                + "<p>This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh?</p>\n"
                + "\n"
                + "<h2>This is a header <a href=\"http://yahoo.com\">with a link</a></h2>"
        )
    );
  }
}
