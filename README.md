# Employee Managment Web Application

#### **Proje Tanıtımı:**
Springboot ve Vaadin kullanılarak hazırlanmış bir Employee Yönetim web uygulamasıdır. İşçileri kaydetme, görüntüleme, arama, düzenleme ve silme işlemleri yapılmaktadır. 

Kullanılan Teknolojileri:
- Java programlama dili
- Springboot Çatısı 
	- RestFUL API
	- JPA
	- Hibernate
	- Spring Validation
	- JUnit
- Vaadin
- H2 Database


#### Projeyi Çalıştırma Adımları  
1️. Depoyu (repository) klonlayın  
```sh
git clone https://github.com/RBaykan/Employee_Managment_Springboot_Vaadin.git
```  

2️. Proje dizinine gidin  
```sh
cd Employee_Managment_Springboot_Vaadin
```  

3️. Bağımlılıkları yükleyin ve projeyi derleyin  
```sh
mvn clean install
```  

4️. Uygulamayı çalıştırın  
```sh
mvn spring-boot:run
```  

5️. Uygulama şimdi çalışıyor olmalıdır  
Açılan servis şu adreste çalışacaktır:  
 **http://localhost:8080**  

 Not: Harici olarak projeyi zip olarak indirip, zip dosyasını arşiv programı ile açın.
 Sonra klasörü Eclipse ve IntelliJ'e aktarıp, çalıştırabileceğiniz gibi Maven ile de çalıştırabilirsiniz.


#### **Uygulamanın Çalışma Akışı**

**`Employee` Modeli:**
- 4 adet değişken barındırır.
	- ID benzersiz model olduğunu bildirir.
	- TC Kimlik numarası, eşsiz olacağı belirtilmiştir.
	- Ad ve Soyad işçinin diğer özellikleridir.

**RestFUL API'ler:**
- Gerekli CRUD operasyonlarını yapmak için RestFUL API metotları oluşturuldu.
	- Listeleme için cache özelliği kullandığı. 
	- Oluşturma, güncelleme ve silme istekleri olmadığı sürece işçi listesi almak için sürekli veri tabanına istek atılmayacaktır.
	- Tüm metotlar, bir `ResponseEntity` olup, ilgili hataları inceler ve kullanıcıya bildirir.
- Bu API metotları `EmployeeController` modelinde toplandı.
- Vaadin ve dışarıdan gelecek istekleri için kullanıldı.

**İşçi oluşturma:**
- Vaadin ile hazırlanan UI'da, işçi oluşturmak için 3 adet girdi alanı vardır. Bu alanlara sırasıyla TC Kimlik Numarası, ad ve soyad girilmedi. 
- Bu girdilerin nasıl doğrulacağını `EmployeeDTO` modelinde belirtildi.
	- TCKN -> @TCValid adında annoastasyon oluşturduk. Bu annotasyon ile TC Kimlik Numarası doğrulama algoritması kullanıldı.
	- Ad ve Soyad için -> @NotBlank annotasyonu kullanıldı.
	- Bu annotasyonları da Vaadin'in bilmesi için, `BeanValidation` modelini kullanıldı
- İşçi oluştururken doğrulama başarılı ise `EmployeeController` modelinin oluşturma metotuna gönderilir. 
- TCKN, Unique bir değer ise veri tabanın kaydedilir. Değilse kullanıcıya bildirilir.
- Başarılı değil ise hata kodları incelenir ve kullanıcıya bildirim verir.

**İşçileri görüntüleme:**
- Vaadin tarafından oluşturulan `Grid` nesnesine, `EmployeeController` sınıfından işçi listesi alınır. Bu liste `Grid` nesnesine aktarıldı ve bir tablo yapıldı.
	- Listeye çekemezse eğer, ilgili hata kodları incelenir ve kullanıcıya bildirim verir.
- Tabloda sütunlar, kullanıcının özelliklerini bildirir.
- Tabloda satırlar, kullanıcıları listeler.
- Kullanıcıyı düzenleme ve silme işlemleri, her kullanıcı için satır sonunda belirtildi.


**İşçileri düzenleme:**
- Tabloda düzenlenmek için seçilen işçinin bilgileri, işçi oluşturma formuna yerleştirilir.
- Düzenleme işlemine geçilir ve oluşturma formundaki doğrulama işlemlerine de geçerli olur.
- Kaydedileceği zaman `EmployeeController` modelinin düzenleme metoduna gönderilir.
- TCKN eğer aynı ise başarıyla kaydeder.
- TCKN aynı değil fakat unique ise kayededer, 
- TCKN aynı değil fakat unique değilse kaydetmez.
- Durumun sonucu kullanıcıya bildirilir.

**İşçi silme:**
- Tabloda silmek için seçilen işçinin ID değeri, `EmployeeController` silme metotuna bildirilir.
- Görüntülendiği için zaten veri tabanında vardır gibi gözükebilir yalnız yine de bir kontrol işlemi gerçekleşir.
- Eğer ID'ye göre işçi bulursa silinir, bulmazsa silinmez.
- Durumun sonucu kullanıcıya bildirilir.

**İşçi arama:**

- Arama için kullanılacak String, Vaadin tarafında yapılan kontrollerden sonra, `EmployeeController` modelinin arama metotuna gönderilir.
- Bu metot işçi listesini yine `EmployeeController` modelinde çeker. 
- Hata olursa kullanıcıya bildirilir.
- Hata olmaz ise arama için kullanılacak String ile işçi listesi, arama algoritmasını gerçekleştirecek bir alt fonksiyonuna parametre olarak gönderilir.
- Arama algoritması String'i keywordlere ayırır ve ona göre bir arama yapar.
- Aramada bulunan işçiler bir listeye aktarılır ve bu liste döndürülür.
- Bu liste eğer boş değil ise Grid'e aktarılır ve tablo oluşturulup, ekranda gösterilir.

# Projeyi Çalıştırma Adımları  

## 1. Depoyu (repository) klonlayın  
```bash
git clone https://github.com/RBaykan/Spring_Security_Register_Email_Verification_Token.git
```

