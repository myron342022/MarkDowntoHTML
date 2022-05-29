package com.mailchimp.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import com.mailchimp.converter.Text.Link;
import com.mailchimp.converter.Text.Words;
import java.util.Optional;
import org.junit.Test;

public class TextTest {


  @Test
  public void testSimpleParagraph() {
    String rawMd = "This is a text";

    Optional<Text> actual = Text.parse(rawMd);

    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Words.builder().words(rawMd).build()
    ));
  }

  @Test
  public void testMultiLineParagraph() {
    String rawMd = "This is line one.\nThis is line two";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Words.builder().words(rawMd).build()
    ));
  }

  @Test
  public void testParagraphParsing() {
    String rawMd = "This is a paragraph [with an inline link](http://google.com). Neat, eh?";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Words.builder().words("This is a paragraph ").build(),
        Link.builder().text("with an inline link").reference("http://google.com").build(),
        Words.builder().words(". Neat, eh?").build()
    ));
  }

  @Test
  public void testParagraphWithOnlyLink() {
    String rawMd = "[Just a link](http://google.com)";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Link.builder().text("Just a link").reference("http://google.com").build()
    ));
  }

  @Test
  public void testLinkWithEmptyText() {
    String rawMd = "[](http://google.com)";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Link.builder().text("").reference("http://google.com").build()
    ));
  }


  @Test
  public void testParagraphWithMultipleLink() {
    String rawMd = "This is a paragraph [with an inline link](http://google.com). Neat, eh? [with an inline link](http://google.com)";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Words.builder().words("This is a paragraph ").build(),
        Link.builder().text("with an inline link").reference("http://google.com").build(),
        Words.builder().words(". Neat, eh? ").build(),
        Link.builder().text("with an inline link").reference("http://google.com").build()
    ));
  }

  @Test
  public void testParagraphWithMultipleLinesAndLines() {
    String rawMd = "This is a \nparagraph [with an inline link](http://google.com). Neat, \neh? [with an inline link 2](http://www2.google.com)";

    Optional<Text> actual = Text.parse(rawMd);
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getTextComponents(), contains(
        Words.builder().words("This is a \nparagraph ").build(),
        Link.builder().text("with an inline link").reference("http://google.com").build(),
        Words.builder().words(". Neat, \neh? ").build(),
        Link.builder().text("with an inline link 2").reference("http://www2.google.com").build()
    ));

  }



}
