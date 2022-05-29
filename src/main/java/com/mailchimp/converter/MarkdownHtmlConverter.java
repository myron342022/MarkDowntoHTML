package com.mailchimp.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkdownHtmlConverter {


  private static String convert(String rawText) {
    StringBuilder htmlBuilder = new StringBuilder();
    MarkdownDoc.parse(rawText).renderAsHtml(htmlBuilder);

    return htmlBuilder.toString();
  }


  public static void main(String[] args) {
    if(args.length != 2) {
      System.out.println("Expected: java -jar target/md-html-converter-1.0-SNAPSHOT-jar-with-dependencies.jar source.md target.html");
      System.exit(1);
    }

    String source = args[0];
    String target = args[1];

    try {
      byte[] markdownBytes = Files.readAllBytes(Paths.get(source));
      String markdown = new String(markdownBytes, StandardCharsets.UTF_8);
      String html = convert(markdown);
      Files.write(Paths.get(target), html.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.out.printf("Failed to read / write to file: %s%n", e.getMessage());
      System.exit(1);
    }

  }

}
