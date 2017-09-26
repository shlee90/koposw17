package ffmpeg_handler;

import java.io.File;

public class MovieTemplate {
	
	private final String FFMPEG_PATH = "C:/ffmpeg/bin/ffmpeg";
	private final String COPIED_IMAGES = "copied_images/";
	private final String PROCESSING_VID = "processing_vid/";
	private final String TEMP = "temp/";
	private final String COMPLETE = "complete/";
	private final String PROCESSED_VIDS = "processed_vids/";
	private String music_path = "C:/Users/SCITMaster/Desktop/heisPirate.mp3";
	
	

	public String getMusic_path() {
		return music_path;
	}


	public void setMusic_path(String music_path) {
		this.music_path = music_path;
	}


	public File[] mkDir(String inputImgsPath, String outputVidPath){
		MakeVideo mv = new MakeVideo(FFMPEG_PATH);
		
		File copiedImagesDir = new File(COPIED_IMAGES); // 렌더링 작업중인 임시 파일들을 저장하는 폴더
		File processingVidDir = new File(PROCESSING_VID); // 렌더링 작업중인 임시 파일들을 저장하는 폴더
		File processedVidsDir = new File(PROCESSED_VIDS); // // 렌더링 작업중인 임시 파일들을 저장하는 폴더
		File tempDir = new File(TEMP);
		File outputDir = new File(new File(outputVidPath).getParent());
		
		if(!copiedImagesDir.exists()) {
			copiedImagesDir.mkdirs();
		}
		if(!processingVidDir.exists()) {
			processingVidDir.mkdirs();
		}
		if(!processedVidsDir.exists()) {
			processedVidsDir.mkdirs();
		}
		if(!outputDir.exists()) {
			outputDir.mkdirs();
		}
		
		File imgs[] = mv.getFileList(inputImgsPath); // 영상으로 만들 이미지 경로
		
		imgs = mv.sortFileList(imgs); // 파일명 순으로 정렬
		
		return imgs;
	}
	
	
	public void firstStep(File[] imgs){
		MakeVideo mv = new MakeVideo(FFMPEG_PATH);
		for (int i = 0; i < imgs.length; i++) {
			String imgName = String.format("%03d", (i+1));
			mv.reformatImg(imgs[i].getPath(), COPIED_IMAGES+imgName+".png", "640", "360");
			
			//인트로
			if (i == 0) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "5");
				mv.insertFadeIn(PROCESSING_VID+imgName+".mp4", PROCESSED_VIDS+imgName+".mp4", "0", "3");
			}
			
			//인트로 맛보기
			else if (i <= 3) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSED_VIDS+imgName+".mp4", "1");
			}
			
			//타이틀
			else if (i <= 5) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "2.5");
				File tempDir = new File(TEMP);
				if(!tempDir.exists()) {
					tempDir.mkdirs();
				}
				if (i == 4) {
					mv.insertFadeOut(PROCESSING_VID+imgName+".mp4", TEMP+imgName+".mp4", "0", "1");
				}else {
					
					mv.insertFadeIn(PROCESSING_VID+imgName+".mp4", TEMP+imgName+".mp4", "0.5", "1.5");
					File[] tempf = mv.getFileList(TEMP);
					mv.writeVidListFile(tempf, TEMP+"input.txt");
					mv.mergeVids(TEMP+"input.txt", TEMP+"merge.mp4");
					mv.insertText(TEMP+"merge.mp4", PROCESSED_VIDS+imgName+".mp4", "default", "default", "default", "default", "center", "center", "0.8", "3");
					mv.deleteDir(tempDir.toString());
				}
			}
			
			//웅장한 장면들
			else if (i <= 10) {
				File tempDir = new File(TEMP);
				if(!tempDir.exists()) {
					tempDir.mkdirs();
				}
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "1.5");
				mv.insertFadeIn(PROCESSING_VID+imgName+".mp4", TEMP+imgName+".mp4", "0", "0.5");
				mv.insertFadeOut(TEMP+imgName+".mp4", PROCESSED_VIDS+imgName+".mp4", "1", "1.5");
				mv.deleteDir(tempDir.toString());
			}
			
			//빠른 장면들
			else if (i <= 17) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSED_VIDS+imgName+".mp4", "0.5");
			}
			
			//홍보 멘트
			else if (i == 18) {
				File tempDir = new File(TEMP);
				if(!tempDir.exists()) {
					tempDir.mkdirs();
				}
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "2");
				mv.insertText(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "default", "default", "default", "default", "center", "center", "0.4", "1.7");
				mv.insertFadeIn(PROCESSING_VID+imgName+"a.mp4", TEMP+imgName+".mp4", "0", "0.5");
				mv.insertFadeOut(TEMP+imgName+".mp4", PROCESSED_VIDS+imgName+".mp4", "1.5", "2");

				mv.deleteDir(tempDir.toString());
			}
			else if (i <= 21) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSED_VIDS+imgName+".mp4", "1");
			}
			else if (i == 22) {
				File tempDir = new File(TEMP);
				if(!tempDir.exists()) {
					tempDir.mkdirs();
				}
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "3");
				mv.insertText(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "Director", "default", "default", "default", "center", "center", "0.4", "1.7");
				mv.insertFadeIn(PROCESSING_VID+imgName+"a.mp4", TEMP+imgName+".mp4", "0", "0.5");
				mv.insertFadeOut(TEMP+imgName+".mp4", PROCESSED_VIDS+imgName+".mp4", "1.5", "2");
				mv.deleteDir(tempDir.toString());
			}
			else if (i <= 26) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSED_VIDS+imgName+".mp4", "1");
			}
			else if (i == 27) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "3");
				mv.insertText(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "Actor1", "default", "default", "default", "center", "center", "1", "2.7");
				mv.insertFadeOut(PROCESSING_VID+imgName+"a.mp4", PROCESSED_VIDS+imgName+".mp4", "2.5", "0.5");
			}
			else if (i == 28) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "3");
				mv.insertText(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "Actor2", "default", "default", "default", "center", "center", "1", "2.7");
				mv.insertFadeOut(PROCESSING_VID+imgName+"a.mp4", PROCESSED_VIDS+imgName+".mp4", "2.5", "0.5");
			}
			else if (i == 29) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "3");
				mv.insertText(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "Actor3", "default", "default", "default", "center", "center", "1", "2.7");
				mv.insertFadeOut(PROCESSING_VID+imgName+"a.mp4", PROCESSED_VIDS+imgName+".mp4", "2.5", "0.5");
			}
			else if (i <= 32) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSED_VIDS+imgName+".mp4", "0.5");
			}
			else if (i == 33) {
				mv.imgToVid(COPIED_IMAGES+imgName+".png", PROCESSING_VID+imgName+".mp4", "5.5");
				mv.insertFadeIn(PROCESSING_VID+imgName+".mp4", PROCESSING_VID+imgName+"a.mp4", "0", "1.5");
				mv.insertFadeOut(PROCESSING_VID+imgName+"a.mp4", PROCESSED_VIDS+imgName+".mp4", "4", "5.5");
			}
		}
	}
	
	public void merging(){
		MakeVideo mv = new MakeVideo(FFMPEG_PATH);
		File[] files = mv.getFileList(PROCESSED_VIDS);
		System.out.println("files : "+files.toString());
		File completeDir = new File(COMPLETE);
		if (!completeDir.exists()) {
			completeDir.mkdirs();
			System.out.println("mkdir");
		}
		mv.writeVidListFile(files, PROCESSED_VIDS+"input.txt");
		System.out.println("writeList");
		mv.mergeVids(PROCESSED_VIDS+"input.txt", COMPLETE+"complete.mp4", music_path);
		
		File com = new File(COMPLETE+"complete.mp4");
		if (com.exists()) {
			mv.deleteDir(COPIED_IMAGES);
			mv.deleteDir(PROCESSING_VID);
			mv.deleteDir(PROCESSED_VIDS);
		}
	}
}
