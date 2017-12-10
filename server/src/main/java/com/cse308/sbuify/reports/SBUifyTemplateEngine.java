package com.cse308.sbuify.reports;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SBUifyTemplateEngine {
    public static final String INVALID_TEMPLATE = "Invalid Template";

    /**
     * This method returns HTML string with values filled from results using the
     * templatePath
     * 
     * @param templatePath
     * @param results
     * @return
     */
    public static String process(String templatePath, List<Object[]> results) {
        String templateToFill = "";
        try {
            templateToFill = new String(Files.readAllBytes(Paths.get(templatePath)));

            String content = "";
            for (Object[] objects : results) {
                String row = "";
                for (Object result : objects) {
                    row += String.format("<td>%s</td>", String.valueOf(result));
                }

                content += String.format("<tr>%s</tr>", row);
            }

            templateToFill = templateToFill.replaceFirst("%TBODY%", content);
        } catch (IOException e) {
            e.printStackTrace();
            return INVALID_TEMPLATE;
        }

        return templateToFill;
    }

}
