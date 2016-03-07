package Utility;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiqibai on 7/4/14.
 */
public class JiebaTokenizer {
    private static JiebaTokenizer instance = null;
    private static JiebaSegmenter segmenter = null;

    WordDictionary dictAdd = WordDictionary.getInstance();
    File file = new File("./library/dictionary.dic");

    private JiebaTokenizer() {
        dictAdd.loadUserDict(file);
        segmenter = new JiebaSegmenter();
    }

    private static synchronized void syncInit() {
        if (instance == null)
            instance = new JiebaTokenizer();
    }

    public static JiebaTokenizer getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    public static String processWithF(String text, JiebaSegmenter.SegMode mode) {
        return segmenter.sentenceProcess(text).toString();
    }

    public static  List<String> process(String text, JiebaSegmenter.SegMode mode) {
        List<String> list = new ArrayList<String>();
        List<SegToken> tokens = segmenter.process(text, mode);
        //String[] segTK = new String[tokens.size()];
        for(int i = 0; i < tokens.size(); i++) {
            String s = tool.getReplacement(tokens.get(i).token);
            if(s.trim().length() > 1)
                list.add(tokens.get(i).token);
        }
        return list;
    }
}
