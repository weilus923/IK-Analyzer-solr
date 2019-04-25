package com.weilus.tests;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * 123
 *
 * @author 刘太全
 * @program IK-Analyzer-solr
 * @date 2019-04-25 09:49
 **/
public class IKAnalyzerTest {

    public static void main(String[] args) throws IOException {
        String text = "基于java语言开发的轻量级的中文分词工具包";
        Analyzer analyzer = new IKAnalyzer(true);
        StringReader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream("",reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()){
                System.out.print(term.toString()+"|");
        }
        reader.close();
        System.out.println();
    }
}
