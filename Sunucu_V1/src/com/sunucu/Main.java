package com.sunucu;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;;

public class Main extends AbstractHandler {

	//public static String baseURL = "http://wallpaperswide.com/download"; // 155.223.26.79:8080
	public static int size = 50; // http://wallpaperswide.com/download
	public static LRUCache<String, BufferedImage> lruCatche = new LRUCache<String, BufferedImage>(size);


	// response.getOutputStream());
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		
		System.out.println(target);
		Map<String, String[]> map = request.getParameterMap();
		BufferedImage img = lruCatche.accessImg(target);
		response.setHeader("Content-Type", "image/jpg");
		ImageIO.write(imgConfig(map, img), "jpg", response.getOutputStream());
		// MAP KONTROLU

	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8088);
		ResourceHandler fileResourceHandler = new ResourceHandler();

		ContextHandler context = new ContextHandler();
		context.setContextPath("/img");

		context.setHandler(new Main());

		server.setHandler(context);
		server.start();
		server.join();
	}

	/*
	 * void sendImage(String path, HttpServletResponse response) throws
	 * IOException { BufferedImage bufferedImage = ImageIO.read(new File(path));
	 * System.out.println("send image" + path);
	 * response.setHeader("Content-Type", "image/jpg");// or png or gif, etc
	 * ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
	 * ///////////////// en son olcak iş URL url = new URL(baseURL);
	 * BufferedImage originalImage = ImageIO.read(url.openStream());
	 * ImageIO.write(originalImage, "jpg", response.getOutputStream());
	 * 
	 * }
	 */
	BufferedImage imgConfig(Map<String, String[]> map, BufferedImage sourceFile) {
		try {
			if (map.size() == 0) {
				return sourceFile;
			} else if (map.size() == 1) {
				// gray işlemi
				return new Scalar().toGray(sourceFile);

			} else if (map.size() == 2) {
				// scale işlemi
				return new Scalar().scale(sourceFile, Integer.parseInt(map.get("width")[0]),
						Integer.parseInt(map.get("height")[0]));

			} else {
				// gray&scale
				return new Scalar().grayAndScale(sourceFile, Integer.parseInt(map.get("width")[0]),
						Integer.parseInt(map.get("height")[0]));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		return sourceFile;
	}
}