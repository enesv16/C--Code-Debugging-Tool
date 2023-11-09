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

		// Class i�indeyken public methodlar� almak i�in public k�sm�n� bulan regex
		// ifadesi
		final String publicBulucuRegex = "public(?=\\s*\\:)";

		// Method isimlerini alan regex ifadesi
		final String methodBulucuRegex = "(?<!(\\!|\\(|>|:|\\.|new ?|return ?|=|-|, ?))(?!\\b(if|while|for|switch|main)\\b)(\\*?\\~?\\b\\w+)(?=(\\<\\<)?\\s*\\()(.*)(\\)\\s*)(?![;|,])";

		// Methodlar�n d�n�� t�rlerini alan regex ifadesi
		final String donusTuruRegex = "(void|string|double|int|bool|ostream&|\\~)\\s?(\\b\\w+)(?=\\s*(\\<\\<)?\\()";

		// Methodlar�n parantez i�lerini alan regex ifadesi
		final String parantezBulucuRegex = "\\( *([^)]+?) *\\)";

		// Parantez i�indeki parametrelerin t�rlerini alan regex ifadesi
		final String parametreBulucuRegex = "(?<=(\\,|\\())\\s*?(\\w+\\s*[\\*|\\&]?)";

		// Class'�n biti�ini bulmama yard�mc� olan regex ifadesi
		final String classSonuRegex = "\\}\\s*\\;";

		// Super Classlar�n isimlerini bulan regex ifadesi
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

			while (sc.hasNext()) { // Dosya bitine kadar true d�nen while d�ng�s�

				classMatcher = classPattern.matcher(metinBelgesi);

				superClassMatcher = superClassPattern.matcher(metinBelgesi);

				methodMatcher = methodPattern.matcher(metinBelgesi);

				donusMatcher = donusPattern.matcher(metinBelgesi);

				parantezMatcher = parantezPattern.matcher(metinBelgesi);

				classSonuMatcher = classSonuPattern.matcher(metinBelgesi);

				if (!(metinBelgesi.contains("main"))) { // Main'e gelince program� bitiren ko�ul ifadesi

					if (classMatcher.find()) { // Class bulunduysa �al��an ko�ul ifadesi

						if (superClassMatcher.find()) { // S�per class carsa �al��an ko�ul ifadesi

							if (classKalitim.contains(superClassMatcher.group(1).trim())) { // Kal�t�m al�nan s�nf�lar�n ka� kez kal�t�m al�nd���n� kontrol eden ko�ul 

								superClassSayac = classKalitim.indexOf(superClassMatcher.group(1).trim());
								kalitimSayac[superClassSayac] += 1;
							} else {

								classKalitim.add(superClassMatcher.group(1).trim());
								superClassSayac = classKalitim.indexOf(superClassMatcher.group(1).trim());
								kalitimSayac[superClassSayac] = 1;
							}

						}

						System.out.println("S�n�f: " + classMatcher.group(1)); // Bulunan Class ismi yaz�l�yor 

						while (!(publicMatcher.find())) { // Bulunan class i�inde public methodlar�n�� almak i�in publice gelene kadar alt sat�ra inen d�ng�

							metinBelgesi = sc.nextLine();
							publicMatcher = publicPattern.matcher(metinBelgesi);
						}
						while (!(classSonuMatcher.find())) { // Class'�n biti�ine kadar d�nen d�ng�

							metinBelgesi = sc.nextLine();

							methodMatcher = methodPattern.matcher(metinBelgesi);

							donusMatcher = donusPattern.matcher(metinBelgesi);

							parantezMatcher = parantezPattern.matcher(metinBelgesi);

							classSonuMatcher = classSonuPattern.matcher(metinBelgesi);

							if (methodMatcher.find()) { // Okunan sat�rda metod ismini bulan ko�ul

								System.out.println("\n" + "	   " + methodMatcher.group(3));

								if (parantezMatcher.find()) { // Okunan sat�rda methodun parantez i�ini kontrol eden ko�ul

									parantezIci = parantezMatcher.group(0);

									parametreMatcher = parametrePattern.matcher(parantezIci);

									while (parametreMatcher.find()) { // Bulunan parametre t�rlerinin hepsini d�nen ko�ul

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

								if (donusMatcher.find()) { // Bulunan methodun d�n�� t�r�n� kontrol eden ko�ul

									if (donusMatcher.group(1).contains("~")) {

										System.out.println("	   	   D�n�� T�r�: void");

									} else {

										System.out.println("	   	   D�n�� T�r�: " + donusMatcher.group(1));
									}

								} else {

									System.out.println("	   	   D�n�� T�r�: Nesne Adresi");
								}

							}

						}

					}
				} else {
					break;
				}

				metinBelgesi = sc.nextLine();
			}

			System.out.println("S�per S�n�flar:");

			for (int i = 0; i < classKalitim.size(); i++) { // Buluna s�per s�n�flar� arraylistten yazd�ran for f�ng�s�

				System.out.println("		" + classKalitim.get(i) + ": " + kalitimSayac[i]);
			}

		} catch (FileNotFoundException e) {

			System.err.println("Hata:   " + e);
		}

	}

}