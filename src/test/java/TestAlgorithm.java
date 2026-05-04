import Amr.Construction.RuleBasedConstructionAlgorithm;
import AnnotatedSentence.AnnotatedSentence;
import WordNet.WordNet;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TestAlgorithm {

    @Test
    public void testAlgorithm(){
        RuleBasedConstructionAlgorithm algorithm = new RuleBasedConstructionAlgorithm(new WordNet());
        AnnotatedSentence sentence0 = new AnnotatedSentence(new File("sentences/0000.test"));
        assertEquals("0000.test\tO her zaman yemeklerini lokantada yer .\n" +
                "6/ye\n" +
                "\t1/o:ARG0\n" +
                "\t\t2/her 3/zaman\n" +
                "\t4/yemek:ARG1\n" +
                "\t\to:poss\n" +
                "\t5/lokanta:location\n", algorithm.toString(sentence0));
        AnnotatedSentence sentence1 = new AnnotatedSentence(new File("sentences/0001.test"));
        assertEquals("0001.test\tDün çocuklar okula gitti mi ?\n" +
                "4/git\n" +
                "\t\t1/dün\n" +
                "\t2/çocuk:ARG0\n" +
                "\t3/okul:ARG1\n" +
                "\t\t5/mi\n", algorithm.toString(sentence1));
        AnnotatedSentence sentence2 = new AnnotatedSentence(new File("sentences/0002.test"));
        assertEquals("0002.test\tHer gün nereye gidiyorsun ?\n" +
                "4/git\n" +
                "\tsen:ARG0\n" +
                "\t\t1/her 2/gün\n" +
                "\t3/nere:ARG1\n", algorithm.toString(sentence2));
        AnnotatedSentence sentence3 = new AnnotatedSentence(new File("sentences/0003.test"));
        assertEquals("0003.test\tEğer sokağa çıkmak istiyorsan …\n" +
                "4/iste:cond\n" +
                "\tsen:ARG0\n" +
                "\t\t1/eğer\n" +
                "\t2/sokak 3/çık:ARG1\n" +
                "\t\to:ARG0\n", algorithm.toString(sentence3));
        AnnotatedSentence sentence4 = new AnnotatedSentence(new File("sentences/0004.test"));
        assertEquals("0004.test\tBugün Ziya'yla buluştuğun zaman …\n" +
                "4/zaman\n" +
                "\t\t3/buluş\n" +
                "\t\tsen:poss\n" +
                "\t\t\t\t1/bugün\n" +
                "\t\tperson:ARG1\n" +
                "\t\t\tname:name\n" +
                "\t\t\t\t2/ziya:op1\n" +
                "\t\t\t-:wiki\n", algorithm.toString(sentence4));
        AnnotatedSentence sentence5 = new AnnotatedSentence(new File("sentences/0005.test"));
        assertEquals("0005.test\tEve gittim , çantamı aldım …\n" +
                "and\n" +
                "\t\t5/al:op1\n" +
                "\t\tben:ARG0\n" +
                "\t\t4/çanta:ARG1\n" +
                "\t\t\tben:poss\n" +
                "\t\t2/git:op2\n" +
                "\t\tben:ARG0\n" +
                "\t\t\t\t1/ev\n", algorithm.toString(sentence5));
        AnnotatedSentence sentence6 = new AnnotatedSentence(new File("sentences/0006.test"));
        assertEquals("0006.test\tAydın bana çiçek getirdi .\n" +
                "4/getir\n" +
                "\tcity:ARG0\n" +
                "\t\tname:name\n" +
                "\t\t\t1/aydın:op1\n" +
                "\t\taydın:wiki\n" +
                "\t\t2/ben\n" +
                "\t3/çiçek:ARG1\n", algorithm.toString(sentence6));
        AnnotatedSentence sentence7 = new AnnotatedSentence(new File("sentences/0007.test"));
        assertEquals("0007.test\tŞu anahtarı hanginize vereyim ?\n" +
                "4/ver\n" +
                "\tben:ARG0\n" +
                "\t2/anahtar:ARG1\n" +
                "\t\t\t\t1/şu\n" +
                "\t3/hangi:ARG2\n" +
                "\t\tbiz:poss\n", algorithm.toString(sentence7));
        AnnotatedSentence sentence8 = new AnnotatedSentence(new File("sentences/0008.test"));
        assertEquals("0008.test\tDışarıda bir köpek var .\n" +
                "4/var\n" +
                "\t1/dışarı:mod\n" +
                "\t\t3/köpek\n", algorithm.toString(sentence8));
        AnnotatedSentence sentence9 = new AnnotatedSentence(new File("sentences/0009.test"));
        assertEquals("0009.test\tLondra'ya mı gideceksiniz ?\n" +
                "3/git\n" +
                "\tsiz:ARG0\n" +
                "\tcity:ARG1\n" +
                "\t\tname:name\n" +
                "\t\t\t1/londra:op1\n" +
                "\t\tlondra:wiki\n" +
                "\t\t\t\t2/mi\n", algorithm.toString(sentence9));
        AnnotatedSentence sentence10 = new AnnotatedSentence(new File("sentences/0010.test"));
        assertEquals("0010.test\tLondra'ya gidecek misiniz ?\n" +
                "2/git\n" +
                "\to:ARG0\n" +
                "\tcity:ARG1\n" +
                "\t\tname:name\n" +
                "\t\t\t1/londra:op1\n" +
                "\t\tlondra:wiki\n" +
                "\t\t3/mi\n", algorithm.toString(sentence10));
    }
}
