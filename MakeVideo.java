/*
 * Made by BOORO!!!!!! MY NAME IS BOORO!!!!!
 * 
 * ++++++++++++++++++++++사용법++++++++++++++++++++++++++
 * 생성자에 차례대로 ffmpeg 경로, 비디오화 할 이미지들의 디렉토리 경로(예:../), 생성될 파일 경로(예:../파일이름.mp3), 삽입할 음악 경로(예:../xx.mp3) 를 매개변수로 삽입한다.
 * 그 후 render() 실행
 *  
 */

package ffmpeg_handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;

public class MakeVideo {
	private String ffmpegPath;

	public MakeVideo(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	public String getFfmpegPath() {
		return ffmpegPath;
	}

	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	public void insertText(String inputPath, String outputPath, String text, String fontfile, 
			String fontcolor, String fontsize, String x, String y, String st, String et) {
		String fontPath = "/Windows/Fonts/";
		if (text.equals("default")) { text = "title"; 	}
		if (fontfile.equals("default")) { fontfile = "Arial.ttf"; 	}
		if (fontcolor.equals("default")) { fontcolor = "white"; 	}
		if (fontsize.equals("default")) { fontsize = "40"; 	}
		if (x.equals("center")) { x = "(w-tw)/2"; 	}
		if (y.equals("center")) { y = "(h/PHI)"; 	}
		String[] code = { ffmpegPath, "-i", inputPath, "-vf",
				"drawtext=fontfile="+fontPath+fontfile
				+":text='"+text
				+"':fontcolor="+fontcolor
				+":fontsize="+fontsize
				+":x="+x
				+":y="+y
/*				+":box=1"
				+ ":boxcolor=black"
				+ ":boxborderw=640"
				+ ":line_spacing=360"			*/
				+":enable='between(t,"+st+","+et+")'",
				"-codec:a", "copy", outputPath };
		executeCmd(code);
	}

	public void reformatImg(String inputPath, String outputPath, String width, String height) {
		String[] code = new String[] {
				ffmpegPath, "-i", inputPath, "-vf", "scale=" + width + ":" + height
						+ ":force_original_aspect_ratio=decrease,pad=" + width + ":" + height + ":(ow-iw)/2:(oh-ih)/2",
				outputPath };

		executeCmd(code);
	}

	/**
	 * 폴더삭제하는 메소드
	 */
	public void deleteDir(String path) {
		Path directory = Paths.get(path);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 경로에서 파일 리스트를 불러온다
	 */
	public File[] getFileList(String path) {

		File dirFile = new File(path);
		File[] fileList = dirFile.listFiles();

		return fileList;
	}

	/**
	 * 파일을 이름 순으로 정렬한다
	 */
	public File[] sortFileList(File[] files) {

		Arrays.sort(files, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {

				String s1 = "";
				String s2 = "";

				s1 = ((File) object1).getName();
				s2 = ((File) object2).getName();

				return s1.compareTo(s2);
			}
		});

		return files;
	}

	/**
	 * concat demuxer 텍스트파일을 만든다.
	 */
	public void writeVidListFile(File[] vids, String path) {
		File file = new File(path);

		// true 지정시 파일의 기존 내용에 이어서 작성
		try {
			FileWriter fw = new FileWriter(file, false);

			String txt = new String();

			for (int i = 0; i < vids.length; i++) {

				txt += "file '";
				txt += vids[i].getName();
				txt += "'";
				if (i < vids.length - 1) {
					txt += "\r\n";
				}
			}

			fw.write(txt);
			fw.flush();

			// 객체 닫기
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 만들어진 concat demuxer 텍스트파일을 이용해 모든 동영상을 합친다. 음악 없는 버전
	 */
	public void mergeVids(String inputPath, String outputPath) {

		String[] code = { ffmpegPath, "-y", "-f", "concat", "-safe", "0", "-i", inputPath, "-c", "copy", outputPath };

		executeCmd(code);
	}

	/**
	 * 만들어진 concat demuxer 텍스트파일을 이용해 모든 동영상을 합친다. 음악 있는 버전
	 */
	public void mergeVids(String inputPath, String outputPath, String musicPath) {

		String[] code = { ffmpegPath, "-y", "-f", "concat", "-safe", "0", "-i", inputPath, "-i", musicPath, "-c",
				"copy", "-shortest", outputPath };

		executeCmd(code);

	}

	/**
	 * 동일 이미지를 2개로 복사한다
	 */
	public void copyImg(String originalFilePath, String copyFilePath) {
		try {
			final InputStream input = new FileInputStream(originalFilePath);
			final OutputStream output = new FileOutputStream(copyFilePath);
			// get an channel from the stream
			final ReadableByteChannel inputChannel = Channels.newChannel(input);
			final WritableByteChannel outputChannel = Channels.newChannel(output);
			// copy the channels
			ChannelTools.fastChannelCopy(inputChannel, outputChannel);
			// closing the channels
			inputChannel.close();
			outputChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 이미지를 동영상으로 렌더링한다. durationSec은 재생시간(초)
	 */
	public void imgToVid(String inputImgPath, String outputVidPath, String durationSec) {
		String[] code = { ffmpegPath, "-framerate", "1/"+durationSec, "-i", inputImgPath, "-c:v", "libx264", "-r", "30", "-y",
				"-pix_fmt", "yuv420p", outputVidPath };
		executeCmd(code);
	}

	/**
	 * 영상에 Fade in 트렌지션을 삽입한다. startFrame은 시작프레임, durationFrame 재생시간(프레임) 1초 =
	 * 30프레임. (startFrame + durationFrame) / 30 이 동영상 재생시간을 넘지 않도록 할것
	 */
	public void insertFadeIn(String inputVidPath, String outputVidPath, String startFrame, String durationFrame) {
		String[] code = { ffmpegPath, "-i", inputVidPath, "-y", 
				"-vf", "fade=t=in:st=" + startFrame + ":d=" + durationFrame, 
				outputVidPath };
		executeCmd(code);
	}

	/**
	 * 영상에 Fade out 트렌지션을 삽입한다. startFrame은 시작프레임, durationFrame 재생시간(프레임) 1초 =
	 * 30프레임. (startFrame + durationFrame) / 30 이 동영상 재생시간을 넘지 않도록 할것
	 */
	public void insertFadeOut(String inputVidPath, String outputVidPath, String startFrame, String durationFrame) {
		String[] code = { ffmpegPath, "-i", inputVidPath, "-y", "-vf", "fade=t=out:st=" + startFrame + ":d=" + durationFrame,
				outputVidPath };
		executeCmd(code);
	}

	/**
	 * cmd에서 ffmpeg 명령어를 실행하는 메소드
	 */
	public void executeCmd(String[] code) {

		try {

			Process processDuration = new ProcessBuilder(code).redirectErrorStream(true).start();

			StringBuilder strBuild = new StringBuilder();

			try (

					BufferedReader processOutputReader = new BufferedReader(
							new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()));) {

				String line;

				while ((line = processOutputReader.readLine()) != null) {
					strBuild.append(line + System.lineSeparator());
				}
				processDuration.waitFor();

				processOutputReader.close();
			}

			String outputJson = strBuild.toString().trim();

			processDuration.destroy();

			System.out.println(outputJson);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
