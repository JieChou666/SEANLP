package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.BurmesePOS;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.dictionary.language.BurmeseCommonDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractBurmeseSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.ReverseMinimumMatcher;

/**
 * 缅甸语逆向最小匹配算法分词
 * @author Zhao Shiyu
 *
 */
public class BurmeseRevMinMatchSegmenter  extends AbstractBurmeseSegmenter {
	
	private Matcher revMinMatcher = new ReverseMinimumMatcher(BurmeseCommonDictionary.burmeseDictionary.dictionaryTrie);
	private final POS pos = new BurmesePOS();
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		if (Config.BaseConf.speechTagging) return pos.speechTagging(revMinMatcher.segment(sentence));
		return revMinMatcher.segment(sentence);
	}
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		if (Config.BaseConf.speechTagging) return pos.speechTagging(revMinMatcher.segment(sentence));
		return revMinMatcher.segment(sentence);
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		BurmeseRevMinMatchSegmenter seg = new BurmeseRevMinMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		System.err.println(text);
		System.out.println(line.replaceAll("\\|", ""));
		
	}

}
