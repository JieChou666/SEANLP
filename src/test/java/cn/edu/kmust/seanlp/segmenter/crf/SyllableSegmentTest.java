package cn.edu.kmust.seanlp.segmenter.crf;

import cn.edu.kmust.seanlp.Config;
import cn.edu.kmust.seanlp.segmenter.ThaiSegmenter;
import cn.edu.kmust.seanlp.segmenter.CRF.ThaiCRFSegmenter;
import junit.framework.TestCase;

public class SyllableSegmentTest extends TestCase {
	
	public void testSyllableSegment() {
		Config.BaseConf.enableDebug();
		ThaiSegmenter segmentThai = new ThaiCRFSegmenter();
		String textThai = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		//ความ|สัมพันธ์|ใน|ทาง|เศรษฐกิจ|กับ|ระบบ|ความ|สัมพันธ์|ทาง|กฎหมาย|
		//ความ|สัมพันธ์|ใน|ทาง|เศรษฐกิจ|กับระบบ|ความ|สัมพันธ์|ทาง|กฎหมาย|
		
		String ret = segmentThai.syllableSegment(textThai);
		System.out.println("音节切分结果：" + ret);
	}

}
