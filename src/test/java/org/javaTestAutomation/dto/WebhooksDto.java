package org.javaTestAutomation.dto;

import java.util.List;

public class WebhooksDto {
        public List<WebhookDataEntry> data;
        public int total;

    public static class WebhookDataEntry {
        public String content;
    }
}
