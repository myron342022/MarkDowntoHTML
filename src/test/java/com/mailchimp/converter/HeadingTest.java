package com.mailchimp.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import com.mailchimp.converter.Text.Link;
import com.mailchimp.converter.Text.Words;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;

public class HeadingTest {


  @Test
  public void testHeadingParsing() {
    Optional<Heading> actual = Heading.parse("# Header 1");

    assertThat(actual.isPresent(), is(true));
    Heading actualHeading = actual.get();

    assertThat(actualHeading.getLevel(), is(1));
    assertThat(actualHeading.getText().getTextComponents(), contains(
        Words.builder().words("Header 1").build()
    ));
  }


  @Test
  public void testHeadingWithoutSpaceParsing() {
    Optional<Heading> actual = Heading.parse("#Header 1");
    assertThat(actual.isPresent(), is(true));
    Heading actualHeading = actual.get();

    assertThat(actualHeading.getLevel(), is(1));
    assertThat(actualHeading.getText().getTextComponents(), contains(
        Words.builder().words("Header 1").build()
    ));
  }

  @Test
  public void testHeadingWithMultipleSpaces() {
    Optional<Heading> actual = Heading.parse("##    Heading");

    assertThat(actual.isPresent(), is(true));
    Heading actualHeading = actual.get();

    assertThat(actualHeading.getLevel(), is(2));
    assertThat(actualHeading.getText().getTextComponents(), contains(
        Words.builder().words("Heading").build()
    ));
  }

  @Test
  public void testHeadingLevelN() {

    for (int i = 1; i <= 6; i++) {
      char[] head = new char[i];
      Arrays.fill(head, '#');
      String markDown = new String(head);
      markDown += " Some text";

      Optional<Heading> actual = Heading.parse(markDown);

      assertThat(actual.isPresent(), is(true));
      assertThat(actual.get().getLevel(), is(i));
      assertThat(actual.get().getText().getTextComponents(), contains(
          Words.builder().words("Some text").build()
      ));
    }


  }

  @Test
  public void testHeadingWithLinks() {
    Optional<Heading> actual = Heading.parse("## This is a header [with a link](http://yahoo.com)");

    assertThat(actual.isPresent(), is(true));
    Heading actualHeading = actual.get();

    assertThat(actualHeading.getLevel(), is(2));
    assertThat(actualHeading.getText().getTextComponents(), contains(
        Words.builder().words("This is a header ").build(),
        Link.builder().text("with a link").reference("http://yahoo.com").build()
    ));
  }

  @Test
  public void testHeaderHtml() {
    Optional<Heading> actual = Heading.parse("# Header 1");

    assertThat(actual.isPresent(), is(true));
    Heading actualHeading = actual.get();

    StringBuilder sb = new StringBuilder();
    actualHeading.renderAsHtml(sb);
    assertThat(sb.toString(), is(
        "<h1>Header 1</h1>"
    ));
  }

}
