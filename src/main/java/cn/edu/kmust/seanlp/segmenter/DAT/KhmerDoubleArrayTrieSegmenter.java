package cn.edu.kmust.seanlp.segmenter.DAT;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.collection.trie.DATrie;
import cn.edu.kmust.seanlp.dictionary.CoreDictionary;
import cn.edu.kmust.seanlp.dictionary.language.KhmerCoreDictionary;
import cn.edu.kmust.seanlp.segmenter.AbstractKhmerSegmenter;
import cn.edu.kmust.seanlp.segmenter.domain.Nature;
import cn.edu.kmust.seanlp.segmenter.domain.Term;

/**
 * 使用DoubleArrayTrie实现的最长分词器
 * 
 * @author  Zhao Shiyu
 *
 */
public class KhmerDoubleArrayTrieSegmenter extends AbstractKhmerSegmenter {
	
	/**
	 * 构造分词器，同时配置语言
	 */
	public KhmerDoubleArrayTrieSegmenter() {
		super();
		Config.BaseConf.useCustomDictionary = false;
	}
	
	@Override
	protected List<Term> segmentSentence(char[] sentence) {
		final int[] wordNet = new int[sentence.length];
		Arrays.fill(wordNet, 1);
		final Nature[] natureArray = Config.BaseConf.speechTagging ? new Nature[sentence.length]	: null;
//		DATrie<String>.Searcher searcher = KhmerCoreDictionary.khmerDictionary.dictionaryTrie.getSearcher(sentence, 0);
		DATrie<CoreDictionary.Attribute>.Searcher searcher = KhmerCoreDictionary.khmerDictionary.dictionaryTrie.getSearcher(sentence, 0);
		while (searcher.next()) {
			int length = searcher.length;
			if (length > wordNet[searcher.begin]) {
				wordNet[searcher.begin] = length;
				if (Config.BaseConf.speechTagging) {
					natureArray[searcher.begin] = searcher.value.nature[0];
				}
			}
		}
		
		if (Config.BaseConf.speechTagging) {
			for (int i = 0; i < natureArray.length;) {
				if (natureArray[i] == null) {
					int j = i + 1;
					for (; j < natureArray.length; ++j) {
						if (natureArray[j] != null)
							break;
					}
					
					List<Term> nodeList = quickSegment(sentence,i, j);
					for (Term node : nodeList) {
						if (node.getWord().length() >= wordNet[i]) {
							wordNet[i] = node.getWord().length();
							natureArray[i] = node.getNature();
							i += wordNet[i];
						}
					}
					i = j;
				} else {
					++i;
				}
			}
		}
		
		LinkedList<Term> termList = new LinkedList<Term>();
		for (int i = 0; i < wordNet.length;) {
			Term term = new Term(new String(sentence, i, wordNet[i]), Config.BaseConf.speechTagging ? (natureArray[i] == null ? Nature.W	: natureArray[i]) : null);
			term.setOffset(i);
			termList.add(term);
			i += wordNet[i];
		}
		return termList;
	}
	
	protected static List<Term> quickSegment(char[] charArray, int start, int end) {
		List<Term> nodeList = new LinkedList<Term>();
		int offsetAtom = start;
		while (++offsetAtom < end) {
			nodeList.add(new Term(new String(charArray, start, offsetAtom - start), null));
			start = offsetAtom;
		}
		if (offsetAtom == end)
			nodeList.add(new Term(new String(charArray, start, offsetAtom - start), null));

		return nodeList;
	}
	
	@Override
	public List<Term> segmentSentence(String sentence) {
		return segmentSentence(sentence.toCharArray());
	}

	@Override
	protected List<Term> segmentSentence(String[] sentence) {
		return null;
	}

}
