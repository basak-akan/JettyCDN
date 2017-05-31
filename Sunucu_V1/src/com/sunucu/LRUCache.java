
package com.sunucu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	// şimdi ben ekstradan şöyle birşey daha istiyorum. Resmin boyutları
	// değişmiş olan yani bizim gelen istek doğrultusunda işlediğimiz
	// resimleride bir bağlı liste gibi birşeyde tutayım. Eğer atıyorum son 10
	// istekte aynı işlem bir kez daha istendiyse direk ordan alayım
	// kullanıcıya döndüreyim.Onu bu classta mı yaparız ona emin değilim.

	private final int cacheSize;
	private final String fileBasePath;
	private final String baseURL;

	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		// BU METHOD LINKED HASH MAP TARAFINDAN PUT VE PUTALL METHODLARI
		// KULLANILDIĞINDA OTOMATIK CAĞIRILIR.
		// acaba burda eldesti de dosyaya yazabilirmiyiz.
		/*if (size() > cacheSize) {
			// file yaz
			try {
				ImageIO.write((BufferedImage)eldest.getValue(), "jpg", new File(fileBasePath + eldest.getKey().toString()));
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false; */
		 return size() > cacheSize;
	}
	public LRUCache(int cacheSize) {
		this.cacheSize = cacheSize;
		this.fileBasePath="/home/basak/Desktop/Sunucu_V1/img";
		this.baseURL = "http://bihap.com/img";  //http://bihap.com/img http://wallpaperswide.com/download
	}
	public V accessImg(K key) throws IOException {
		V value = null;
		BufferedImage img;
		if (super.containsKey(key)) {
			// Eğer dosya hashmapte varsa direk bul getir.
			value = super.get(key);
			// acaba bu iş threade verilebilir mi?? senkron değil çünkü.
			super.remove(key);
			super.put(key, value);
			return value;
		} else {
			// eğer hashmapte yoksa dosyaya bak.dosyada varsa dosyadan bul
			// getir.
			// eğer resim diskte varsa bunu hashmape ekle
			// burda bir sorun olablir belki bir daha bi kontrol et
			File file = new File (fileBasePath + key.toString());
			
			if (file.exists())
			{
				img = ImageIO.read(file);
			} 
			else {
				URL url = new URL(baseURL + key.toString()+"/");
				img =( ImageIO.read(url.openStream()));
				
				ImageIO.write(img, "jpg", new File(fileBasePath + key.toString()));

				// BAŞAĞIN DEDİĞİ ŞEY
				// this.put(key, (V) img);

				// webe bak.Webten Çek return yaptır.
				// ahanda buraya ekle return null degıl webten okunan resım
				// doncek.
			}
			super.put(key, (V) img);
			return (V) img;
		}
	}


}


