package cn.edu.kmust.seanlp.segmenter.matcher.language;

import java.util.ArrayList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.POS.POS;
import cn.edu.kmust.seanlp.POS.ThaiPOS;
import cn.edu.kmust.seanlp.dictionary.language.ThaiCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Term;
import cn.edu.kmust.seanlp.segmenter.matcher.Matcher;
import cn.edu.kmust.seanlp.segmenter.matcher.MaximumMatcher;

/**
 * 泰语正向最大匹配算法分词
 * @author Zhao Shiyu
 *
 */
public class ThaiMaxMatchSegmenter extends AbstractThaiSegmenter {
	
	private Matcher maxMatcher = new MaximumMatcher(ThaiCoreDictionary.thaiDictionary.dictionaryTrie);
	private POS pos = new ThaiPOS();
	
	protected List<Term> segment(String[] strs) {
		List<Term> ret = maxMatcher.segment(strs);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}

	@Override
	protected List<Term> sentenceMerge(String[] sentences) {
		List<Term> terms = new ArrayList<Term>();
		int len = sentences.length;
		for (int i = 0; i < len; i++) {
			terms.addAll(segment(toTCC(sentences[i])));
		}
		return terms;
	}
	
	@Override
	protected List<Term> segment(char[] chars) {
		List<Term> ret = maxMatcher.segment(chars);
		if (Config.BaseConf.speechTagging) {
			ret = pos.speechTagging(ret);
		}
		return ret;
	}
	
	@Override
	public List<Term> segment(String text) {
		return sentenceMerge(sentenceSegment(text));
	}
	
	public static void main(String[] args) {
		Config.BaseConf.enableDebug();
		String text = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
//		String text = "我是一个学生！";
		ThaiMaxMatchSegmenter seg = new ThaiMaxMatchSegmenter();
		System.out.println(seg.segment(text));
		String line = "";
		for (Term term : seg.segment(text)) {
			line += term.getWord() + "|";
		}
		System.err.println(text);
		System.out.println(line);
		
	}
	
	@Override
	public String syllableSegment(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> dCRFWordSegment(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> gCRFWordSegment(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> seg(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String syllableSegment(String[] sentences) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected StringBuffer syllableSegment(char[] chars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] sentenceTosyllables(char[] chars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] sentenceTosyllables(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Term> syllableMerge(String[] sentences) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Term> syllableMerge(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Term> syllableMerging(String[] syllables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Term> gCRFWordSegment(String[] sentences) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Term> gCRFWordSegment(char[] chars) {
		// TODO Auto-generated method stub
		return null;
	}

}
