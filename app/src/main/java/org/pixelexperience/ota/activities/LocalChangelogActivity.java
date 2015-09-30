/*
    Copyright (C) 2018 Pixel Experience

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.pixelexperience.ota.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalChangelogActivity extends Activity {

    private static final String CHANGELOG_PATH = "/system/etc/Changelog.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        InputStreamReader inputReader = null;
        String text = "";

        StringBuilder data = new StringBuilder();
        Pattern p = Pattern.compile("([a-f0-9]{7})\\s\\s(.*)\\s\\s\\[(.*)\\]"); //(?dms)
        Pattern p2 = Pattern.compile("\\s+\\*\\s(([\\w_\\-]+/)+)");
        Pattern p3 = Pattern.compile("(\\d\\d\\-\\d\\d\\-\\d{4})");
        try {
            char tmp[] = new char[2048];
            int numRead;

            inputReader = new FileReader(CHANGELOG_PATH);
            while ((numRead = inputReader.read(tmp)) >= 0) {
                data.append(tmp, 0, numRead);
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (IOException ignored) {
            }
        }

        SpannableStringBuilder sb = new SpannableStringBuilder(data);
        Matcher m = p.matcher(data);
        while (m.find()){
            sb.setSpan(new ForegroundColorSpan(Color.rgb(96,125,139)),m.start(1), m.end(1), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD),m.start(1),m.end(1),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new ForegroundColorSpan(Color.rgb(69,90,100)),m.start(3), m.end(3), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        m = p2.matcher(data);
        while (m.find()){
            sb.setSpan(new StyleSpan(Typeface.BOLD),m.start(1), m.end(1), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new ForegroundColorSpan(Color.rgb(33,39,43)),m.start(1),m.end(1),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        m = p3.matcher(data);
        while (m.find()){
            sb.setSpan(new StyleSpan(Typeface.BOLD+ Typeface.ITALIC),m.start(1), m.end(1), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        final TextView textView = new TextView(this);
        textView.setText(sb);

        final ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);

        setContentView(scrollView);
    }
}