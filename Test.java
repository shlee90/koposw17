package ffmpeg_handler;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 사용법 : 생성자에 차례대로 ffmpeg
		MakeVideo test = new MakeVideo("C:/ffmpeg/bin/ffmpeg");
		MovieTemplate mt = new MovieTemplate();
		File[] imgs = mt.mkDir("C:/Users/SCITMaster/Desktop/movie", "C:/Users/SCITMaster/Desktop/pirateMovie.mp4");
		mt.firstStep(imgs);
		mt.merging();
	}
}