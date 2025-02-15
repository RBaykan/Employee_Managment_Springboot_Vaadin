package com.example.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.application.dto.EmployeeDTO;
import com.example.application.exception.EmployeesListNull;
import com.example.application.exception.SearchedTextNull;

public class EmployeeSearchTest {

	private List<EmployeeDTO> employeeDTOS;

	@BeforeEach
	void listEmployee() {

		Long id = 1L;
		employeeDTOS = new ArrayList<EmployeeDTO>();

		employeeDTOS.add(new EmployeeDTO(id, "56790372278", "Vedat", "Ercan"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "70845529066", "Cansu", "Su"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "48961626754", "Ahmet", "Dursun"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "14453523412", "İpek", "Demir"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "48909214760", "Ali", "Yılan"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "93012037836", "Ayşe", "Kul"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "43052039836", "Ayşe", "Dut"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "34062837538", "Ayşe", "Yılan"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "67368454272", "Veysel", "Yurt"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "45454873640", "Firdevs", "Ziyagil"));
		id++;
		employeeDTOS.add(new EmployeeDTO(id, "26844636380", "Mustafa", "Koç"));
		id++;

		employeeDTOS.add(new EmployeeDTO(id, "83555114484", "Aysel", "Bağcan"));

		id++;
		
		 for(int i = 0; i<1000000; i++) { employeeDTOS.add(new EmployeeDTO(id, "i",
		 "i", "i")); id++; }
		 
		

	}

	@Test
	@DisplayName("Arama sorgusu null olursa, SearchedTextNull hatası döndürsün ")
	void nullTextInput() {
		assertThrows(SearchedTextNull.class, () -> findEmployee(null, employeeDTOS));
	}
	
	@Test
	@DisplayName("Arama sorgusu bir boş karater girilirse, arama yaptığı listeyi döndürsün ")
	void emptyTextInput() {
		assertTrue(findEmployee("", employeeDTOS) == employeeDTOS);;
	}
	
	@Test
	@DisplayName("Arama sorgusu bir kaç boşluk karakteri girilirse, arama yaptığı listeyi döndürsün ")
	void oneSpaceTextInput() {
		assertTrue(findEmployee(" ", employeeDTOS) == employeeDTOS);;
	}
	
	@Test
	@DisplayName("Arama sorgusu bir boşluk girilirse, arama yaptığı listeyi döndürsün ")
	void manySpaceTextInput() {
		assertTrue(findEmployee("  ", employeeDTOS) == employeeDTOS);;
	}


	@Test
	@DisplayName("Aranan liste null ise EmployeesListNull fırlatsın")
	void nullListSearching() {
		assertThrows(EmployeesListNull.class, () -> findEmployee("Veysel", null));
	}

	@Test
	@DisplayName("A harfi arandığı zaman listeye 10 adet eleman eklenmelidir")
	void aCharSearch() {

		List<EmployeeDTO> list = findEmployee("A", employeeDTOS);
		assertTrue(list.size() >= 10);
	}

	@Test
	@DisplayName("Listeye göre Aysel arandığı zaman listeye Aysel Bağcan eklesin ve liste uzunluğu 1 olsun")
	void ayselNameSearch() {

		List<EmployeeDTO> list = findEmployee("Aysel", employeeDTOS);
		assertTrue((list.size() == 1) && list.get(0).getFirstname().equals("Aysel")
				&& list.get(0).getLastname().equals("Bağcan"));
	}

	@Test
	@DisplayName("Listeye göre MuStAfA arandığı zaman listeye Mustafa Koç'u eklesin ve liste uzunluğu 1 olsun")
	void MuStAfANameSearch() {

		List<EmployeeDTO> list = findEmployee("MuStAfA", employeeDTOS);

		assertTrue(list.size() == 1 && list.get(0).getFirstname().equals("Mustafa")
				&& list.get(0).getLastname().equals("Koç"));
	}

	@Test
	@DisplayName("Ayşe Kul arandığı zaman, listeye Ayşe Kul eklensin ve liste uzunluğu bir olsun")
	void ayse0SpaceKul() {
		List<EmployeeDTO> list = findEmployee("Ayşe Kul", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Ayşe") && list.get(0).getLastname().equals("Kul")
				&& list.size() == 1);

	}

	@Test
	@DisplayName("Ziyagil         Firdevs arandığı zaman, listeye Ayşe Kul eklensin ve liste uzunluğu bir olsun")
	void ZiyagilManySpaceFirdevs() {
		List<EmployeeDTO> list = findEmployee("Ziyagil         Firdevs", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Firdevs") && list.get(0).getLastname().equals("Ziyagil")
				&& list.size() == 1);

	}
	
	@Test
	@DisplayName("Demir         İpek arandığı zaman, listeye İpek Demir eklensin ve liste uzunluğu bir olsun")
	void demirManySpaceIpek() {
		List<EmployeeDTO> list = findEmployee("Demir         İpek", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("İpek") && list.get(0).getLastname().equals("Demir")
				&& list.size() == 1);

	}
	
	
	@Test
	@DisplayName("Kul         Ayşe arandığı zaman, listeye Ayşe Kul eklensin ve liste uzunluğu bir olsun")
	void kulManySpaceAyse() {
		List<EmployeeDTO> list = findEmployee("Kul         Ayşe", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Ayşe") && list.get(0).getLastname().equals("Kul")
				&& list.size() == 1);

	}
	
	@Test
	@DisplayName("835 arandığı zaman Aysel Bağcan, listeye eklsin ")
	void tc835() {
		List<EmployeeDTO> list = findEmployee("835", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Aysel") && list.get(0).getLastname().equals("Bağcan"));

	}
	
	@Test
	@DisplayName("4890921 yıl arandığı zaman Ali Yılan listeye eklsin ")
	void tc4890921yıl() {
		List<EmployeeDTO> list = findEmployee("4890921 yıl", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Ali") && list.get(0).getLastname().equals("Yılan"));

	}
	
	
	@Test
	@DisplayName("Cansu 70845529066 arandığı zaman Cansu Su listeye eklsin ")
	void cansuTc70845529066() {
		List<EmployeeDTO> list = findEmployee("Cansu 70845529066", employeeDTOS);
		assertTrue(list.get(0).getFirstname().equals("Cansu") && list.get(0).getLastname().equals("Su"));

	}
	
	@Test
	@DisplayName("Ayşe 93012037836 arandığında doğru kişi listeye eklenmeli")
	void ayseAndTcSearch() {
	    List<EmployeeDTO> list = findEmployee("Ayşe 93012037836", employeeDTOS);
	    assertTrue(list.size() == 1 && list.get(0).getFirstname().equals("Ayşe")
	            && list.get(0).getLastname().equals("Kul") && list.get(0).getTckn().equals("93012037836"));
	}
	
	@Test
	@DisplayName("Ali Yılan arandığında doğru kişi listeye eklenmeli")
	void aliAndYilanSearch() {
	    List<EmployeeDTO> list = findEmployee("Ali Yılan", employeeDTOS);
	    assertTrue(list.size() == 1 && list.get(0).getFirstname().equals("Ali")
	            && list.get(0).getLastname().equals("Yılan"));
	}

	@Test
	@DisplayName("Yılan Ali arandığında doğru kişi listeye eklenmeli")
	void yilAndAliSearch() {
	    List<EmployeeDTO> list = findEmployee("Yılan Ali", employeeDTOS);
	    assertTrue(list.size() == 1 && list.get(0).getFirstname().equals("Ali")
	            && list.get(0).getLastname().equals("Yılan"));
	}
	
	@Test
	@DisplayName("   Ahmet        Dursun   arandığında doğru kişi eklenmeli")
	void manySpaceAhmetManySpaceDursunSearch() {
	    List<EmployeeDTO> list = findEmployee("   Ahmet       Dursun  ", employeeDTOS);
	    assertTrue(list.size() == 1 && list.get(0).getFirstname().equals("Ahmet")
	            && list.getFirst().getLastname().equals("Dursun"));
	}

	@Test
	@DisplayName("İpek Demir arandığında doğru kişi eklenmeli (Farklı harfler, karışık yazım)")
	void ipekDemirSearch() {
	    List<EmployeeDTO> list = findEmployee("İpek DEMİR", employeeDTOS);
	    assertTrue(list.size() == 1 && list.getFirst().getFirstname().equals("İpek")
	            && list.getFirst().getLastname().equals("Demir"));
	}
	
	
	@Test
	@DisplayName("1 araması yapıldığında 5 elemanlı  liste dönsün ")
	void tc1() {
	    List<EmployeeDTO> list = findEmployee("1", employeeDTOS);
	    assertTrue(list.size() == 5);
	}
	
	
	
	


	public List<EmployeeDTO> findEmployee(String searchedText, List<EmployeeDTO> empls) {

		if (empls == null) {
			throw new EmployeesListNull();
		}

		if (searchedText == null) {
			throw new SearchedTextNull();
		}
		
		
		if(searchedText.equals(" ") ||  searchedText.trim().isEmpty()){
            return  empls;
        }
		
		
		  
		
		
		searchedText = searchedText.trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
		
		
		
		System.out.println(searchedText);

		String[] keywords = searchedText.split("\\s+");
		Pattern[] p = new Pattern[keywords.length];
		for (int i = 0; i < p.length; i++) {
			
			p[i] = Pattern.compile(Pattern.quote(keywords[i]));
		}

		List<EmployeeDTO> employeesSearched = new ArrayList<>();

		for (var employee : empls) {

			int find = 0;

			String firstname = employee.getFirstname().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
			boolean first = false;
			String lastName = employee.getLastname().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
			boolean last = false;
			String tckn = employee.getTckn().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
			boolean tc = false;
			
			for(int i = 0; i<p.length; i++) {
				
				
				
				if(!first && p[i].matcher(firstname).find()) {
					find++;
					first = true;
					
				}
				
				if(!last && p[i].matcher(lastName).find()) {
					find++;
					last = true;
					
					
				}
				
				if(!tc && p[i].matcher(tckn).find()) {
					find++;
					tc = true;
				
					
				}
				
			}
			
			if( find >= p.length) {
				employeesSearched.add(employee);
			}

		}
		return employeesSearched;

	}

}
