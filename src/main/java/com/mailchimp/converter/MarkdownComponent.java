package com.mailchimp.converter;

public abstract class MarkdownComponent {
  abstract void renderAsHtml(StringBuilder sb);
}
