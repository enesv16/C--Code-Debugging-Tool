/**
*
* @author Muhammet Enes Vardar muhammet.vardar@ogr.sakarya.edu.tr
* @since 9.04.2021
* <p>
* Bir cpp kodunun regex ile analizi.
* </p>
*/
package pdpkt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdpOdev {

	
	public static void main(String[] args) {

		File dosya = new File("src/Program.cpp");

		// Class ismini bulan regex ifadesi
		final String classBulucuRegex = "class\\s*(\\w*)";

		// Class içindeyken public methodlarý almak için public kýsmýný bulan regex
		// ifadesi
		final String publicBulucuRegex = "public(?=\\s*\\:)";

		// Method isimlerini alan regex ifadesi
		final String methodBulucuRegex = "(?<!(\\!|\\(|>|:|\\.|new ?|return ?|=|-|, ?))(?!\\b(if|while|for|switch|main)\\b)(\\*?\\~?\\b\\w+)(?=(\\<\\<)?\\s*\\()(.*)(\\)\\s*)(?![;|,])";

		// Methodlarýn dönüþ türlerini alan regex ifadesi
		final String donusTuruRegex = "(void|string|double|int|bool|ostream&|\\~)\\s?(\\b\\w+)(?=\\s*(\\<\\<)?\\()";

		// Methodlarýn parantez içlerini alan regex ifadesi
		final String parantezBulucuRegex = "\\( *([^)]+?) *\\)";

		// Parantez içindeki parametrelerin türlerini alan regex ifadesi
		final String parametreBulucuRegex = "(?<=(\\,|\\())\\s*?(\\w+\\s*[\\*|\\&]?)";

		// Class'ýn bitiþini bulmama yardýmcý olan regex ifadesi
		final String classSonuRegex = "\\}\\s*\\;";

		// Super Classlarýn isimlerini bulan regex ifadesi
		final String superClassRegex = "class\\s+\\w+\\s*:(\\s*\\w+\\s*)+";

		try {
			Scanner sc = new Scanner(dosya);

			String metinBelgesi = "";
			String parantezIci = "";
			Integer[] kalitimSayac = new Integer[100];
			List<String> classKalitim = new ArrayList<>();
			int parametreSayac = 0;
			int superClassSayac = 0;
			String parametreTutucu = "";

			Pattern classPattern = Pattern.compile(classBulucuRegex, Pattern.MULTILINE);
			Matcher classMatcher = classPattern.matcher(metinBelgesi);

			Pattern publicPattern = Pattern.compile(publicBulucuRegex, Pattern.MULTILINE);
			Matcher publicMatcher = publicPattern.matcher(metinBelgesi);

			Pattern methodPattern = Pattern.compile(methodBulucuRegex, Pattern.MULTILINE);
			Matcher methodMatcher = methodPattern.matcher(metinBelgesi);

			Pattern donusPattern = Pattern.compile(donusTuruRegex, Pattern.MULTILINE);
			Matcher donusMatcher = donusPattern.matcher(metinBelgesi);

			Pattern parantezPattern = Pattern.compile(parantezBulucuRegex, Pattern.MULTILINE);
			Matcher parantezMatcher = parantezPattern.matcher(metinBelgesi);

			Pattern parametrePattern = Pattern.compile(parametreBulucuRegex, Pattern.MULTILINE);
			Matcher parametreMatcher = parametrePattern.matcher(parantezIci);

			Pattern classSonuPattern = Pattern.compile(classSonuRegex, Pattern.MULTILINE);
			Matcher classSonuMatcher = classSonuPattern.matcher(metinBelgesi);

			Pattern superClassPattern = Pattern.compile(superClassRegex, Pattern.MULTILINE);
			Matcher superClassMatcher = superClassPattern.matcher(metinBelgesi);

			while (sc.hasNext()) { // Dosya bitine kadar true dönen while döngüsü

				classMatcher = classPattern.matcher(metinBelgesi);

				superClassMatcher = superClassPattern.matcher(metinBelgesi);

				methodMatcher = methodPattern.matcher(metinBelgesi);

				donusMatcher = donusPattern.matcher(metinBelgesi);

				parantezMatcher = parantezPattern.matcher(metinBelgesi);

				classSonuMatcher = classSonuPattern.matcher(metinBelgesi);

				if (!(metinBelgesi.contains("main"))) { // Main'e gelince programý bitiren koþul ifadesi

					if (classMatcher.find()) { // Class bulunduysa çalýþan koþul ifadesi

						if (superClassMatcher.find()) { // Süper class carsa çalýþan koþul ifadesi

							if (classKalitim.contains(superClassMatcher.group(1).trim())) { // Kalýtým alýnan sýnfýlarýn kaç kez kalýtým alýndýðýný kontrol eden koþul 

								superClassSayac = classKalitim.indexOf(superClassMatcher.group(1).trim());
								kalitimSayac[superClassSayac] += 1;
							} else {

								classKalitim.add(superClassMatcher.group(1).trim());
								superClassSayac = classKalitim.indexOf(superClassMatcher.group(1).trim());
								kalitimSayac[superClassSayac] = 1;
							}

						}

						System.out.println("Sýnýf: " + classMatcher.group(1)); // Bulunan Class ismi yazýlýyor 

						while (!(publicMatcher.find())) { // Bulunan class içinde public methodlarýnýý almak için publice gelene kadar alt satýra inen döngü

							metinBelgesi = sc.nextLine();
							publicMatcher = publicPattern.matcher(metinBelgesi);
						}
						while (!(classSonuMatcher.find())) { // Class'ýn bitiþine kadar dönen döngü

							metinBelgesi = sc.nextLine();

							methodMatcher = methodPattern.matcher(metinBelgesi);

							donusMatcher = donusPattern.matcher(metinBelgesi);

							parantezMatcher = parantezPattern.matcher(metinBelgesi);

							classSonuMatcher = classSonuPattern.matcher(metinBelgesi);

							if (methodMatcher.find()) { // Okunan satýrda metod ismini bulan koþul

								System.out.println("\n" + "	   " + methodMatcher.group(3));

								if (parantezMatcher.find()) { // Okunan satýrda methodun parantez içini kontrol eden koþul

									parantezIci = parantezMatcher.group(0);

									parametreMatcher = parametrePattern.matcher(parantezIci);

									while (parametreMatcher.find()) { // Bulunan parametre türlerinin hepsini dönen koþul

										parametreSayac++;
										parametreTutucu = parametreTutucu + " " + parametreMatcher.group(0);

									}

									System.out.println(
											"	   	   Parametre: " + parametreSayac + " (" + parametreTutucu + ")");

									parametreTutucu = "";
									parametreSayac = 0;

								} else {

									System.out.println("	   	   Parametre: 0");
								}

								if (donusMatcher.find()) { // Bulunan methodun dönüþ türünü kontrol eden koþul

									if (donusMatcher.group(1).contains("~")) {

										System.out.println("	   	   Dönüþ Türü: void");

									} else {

										System.out.println("	   	   Dönüþ Türü: " + donusMatcher.group(1));
									}

								} else {

									System.out.println("	   	   Dönüþ Türü: Nesne Adresi");
								}

							}

						}

					}
				} else {
					break;
				}

				metinBelgesi = sc.nextLine();
			}

			System.out.println("Süper Sýnýflar:");

			for (int i = 0; i < classKalitim.size(); i++) { // Buluna süper sýnýflarý arraylistten yazdýran for föngüsü

				System.out.println("		" + classKalitim.get(i) + ": " + kalitimSayac[i]);
			}

		} catch (FileNotFoundException e) {

			System.err.println("Hata:   " + e);
		}

	}

}