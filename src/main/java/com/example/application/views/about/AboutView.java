package com.example.application.views.about;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("About")
@Route("empty")
@Menu(order = 2, icon = LineAwesomeIconUrl.FILE)
public class AboutView extends VerticalLayout {

    public AboutView() {



        H2 header = new H2("Employee Yönetim Yazılımı");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        H3 subHeader = new H3("Kısaca tanıtım");
        add(subHeader);
        add(new Paragraph("Bu uygulama, çalışanları yönetmek için geliştirilmiş bir sistemdir. " +
                "Kullanıcılar, çalışanları listeleyebilir, arayabilir, düzenleyebilir ve silebilir."
                ));

        setSizeFull();

        subHeader = new H3("Projede kullanılan teknolojiler");
        add(subHeader);
        add(new Paragraph("Backend: Springboot çatısı (Spring, Restful Api, JPA, Hibernate ...)"
        ));
        add(new Paragraph("Database: H2 Database"
        ));
        add(new Paragraph("Front-end: Vaadin Framework"
        ));

        add(new Paragraph("Proje geliştiricisi: Recep Baykan"
        ));


        setSizeFull();

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
