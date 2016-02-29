package cn.edu.kmust.seanlp.corpus.Lao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.kmust.io.IOUtil;
import cn.edu.kmust.seanlp.corpus.util.Corpus;

/**
 * 
 * @author  Zhao Shiyu
 *
 */
public class LaoCorpus {
	
	/**
	 * 从句子语料中获取核心词典
	 * @param sentences
	 * @return
	 */
	public static List<String> makeCoreDictionary(List<String> sentences) {
		Map<String, String> wnmap = new TreeMap<String, String>();
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(">");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("<");
					if (wn.length == 2) {
						String word = wn[0].trim();
						String nature = wn[1].trim();
						if (wnmap.get(word) == null) {
							wnmap.put(word, nature + "\t" + "1");
						} else {
							String[] nm = wnmap.get(word).split("\t");
							boolean catFlag = false;
							for (String cat : nm) {
								if (nature.equals(cat)) {
									catFlag = true;
								}
							}
							
							if (catFlag) {
								int nlen = nm.length;
								for (int j = 0; j < nlen; j += 2) {
									if (nature.equals(nm[j].trim())) {
										//System.out.println("key = " + word + "; nature = " + wnmap.get(word));
										nm[j + 1] = String.valueOf(Integer.parseInt(nm[j + 1]) + 1);
									}
								}
								String newNature = "";
								for (int k = 0; k < nlen; k++) {
									if (k ==0) {
										newNature += nm[k];
									} else {
										newNature += "\t" + nm[k];
									}
								}
								wnmap.put(word, newNature);
								newNature = "";
							} else {
								wnmap.put(word, wnmap.get(word) + "\t" + nature + "\t" + "1");
							}
						}
					}
				}
			}
		}
		return Corpus.mapToList(Corpus.NatureSort(wnmap));
	}
	
	/**
	 * 获取词性集，并排序
	 * @param sentences
	 * @return
	 */
	public static List<String> getNatureSet(List<String> sentences) {
		Map<String, Integer> nmap = new TreeMap<String, Integer>();
		nmap.put("BEGIN", 1); //句始
		nmap.put("END", 1); //句末
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(">");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					String[] wn = words[i].split("<");
					if (wn.length == 2) {
						String nature = wn[1].trim();
						if (nmap.get(nature) == null) {
							nmap.put(nature, 1);
						} else {
							nmap.put(nature, nmap.get(nature) + 1);
						}
					}
				}
			}
		}
		List<String> naturelist = new ArrayList<String>();
		for (String key : nmap.keySet()) {
			naturelist.add(key);
			System.out.println("nature = " + key + "; number = " + nmap.get(key));
		}
		Collections.sort(naturelist);
		return naturelist;
	}
	
	/**
	 * 词性转移矩阵
	 * @param sentences
	 * @param naturelist
	 * @return
	 */
	public static int[][] makeTransitionMatrix(List<String> sentences, List<String> naturelist) {
		Collections.sort(naturelist);
		int[][] transitionMatrix = new int[naturelist.size()][naturelist.size()];
		for (String sentence : sentences) {
			sentence = sentence.trim();
			if (!sentence.isEmpty()) {
				String[] words = sentence.split(">");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					if (i == 0) {
						String[] wn = words[i].split("<");
						if (wn.length == 2) {
							int beginIndex = naturelist.indexOf("BEGIN");
							int natureIndex = naturelist.indexOf(wn[1].trim());
							transitionMatrix[beginIndex][natureIndex] = transitionMatrix[beginIndex][natureIndex] + 1;
						}
					} else if (i == (len - 1)) {
						String[] wn = words[i].split("<");
						if (wn.length == 2) {
							int natureIndex = naturelist.indexOf(wn[1].trim());
							int endIndex = naturelist.indexOf("END");
							transitionMatrix[natureIndex][endIndex] = transitionMatrix[natureIndex][endIndex] + 1;
						}
					} else {
						String[] bword = words[i - 1].split("<");
						String[] word = words[i].split("<");
						if (bword.length == 2 && word.length == 2) {
							int bIndex = naturelist.indexOf(bword[1].trim());
							int natureIndex = naturelist.indexOf(word[1].trim());
							transitionMatrix[bIndex][natureIndex] = transitionMatrix[bIndex][natureIndex] + 1;
						}
					}
				}
			}
		}
		return transitionMatrix;
	}
	
	public static List<String> makeTransitionTable(List<String> naturelist, int[][] transitionMatrix) {
		Collections.sort(naturelist);
		List<String> rets = new ArrayList<String>();
		String line = "";
		int len = naturelist.size();
		// 表头
		for (int i = 0; i < len; i++) {
			line += "\t" + naturelist.get(i);
		}
		rets.add(line);
		line = "";
		//表内容
		for (int i = 0; i < len; i++) {
			line = naturelist.get(i);
			for (int j = 0; j < len; j++) {
				line  += "\t" + transitionMatrix[i][j];
			}
			rets.add(line);
			line = "";
		}
		return rets;
	}
	
	public static List<String> mapToList(Map<String, String> map) {
		List<String> rets = new ArrayList<String>();
		for (String key : map.keySet()) {
			rets.add(key + "\t" + map.get(key));
		}
		return rets;
	}
	
	public static void main(String[] args) {
		String orchidFile = "/home/zsy/developkit/workspace/nlp/seanlp/data/corpus/Lao/lao-corpus.txt";
		List<String> sentences = IOUtil.readLines(orchidFile);
		List<String> natureSet = getNatureSet(sentences);
		List<String> coreDictionary = makeCoreDictionary(sentences);
		int[][] transitionMatrix = makeTransitionMatrix(sentences, natureSet);
		List<String> transitionTable = makeTransitionTable(natureSet, transitionMatrix);
		IOUtil.overwriteLines("data/corpus/Lao/CoreDictionary.Lao.txt", coreDictionary);
//		IOUtil.writeLines("data/corpus/Lao/TransitionMatrix.Lao.txt", transitionTable);
		
		for (String nature : natureSet) {
			System.out.println(nature);
		}
	}
	
}
