package com.blooms.config;

import com.blooms.model.*;
import com.blooms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component @RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final CategoryRepository cats;
    private final ProductRepository products;
    private final PasswordEncoder enc;

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        users.save(User.builder()
                .name("Admin").email("admin@blooms.com")
                .password(enc.encode("Admin@123"))
                .role(User.Role.ADMIN).isActive(true).build());

        users.save(User.builder()
                .name("Priya Florists")
                .email("buyer@blooms.com")
                .password(enc.encode("Buyer@123"))
                .companyName("Priya Florists Pvt Ltd")
                .gstNumber("27ABCDE1234F1Z5")
                .city("Bangalore").state("Karnataka")
                .role(User.Role.BUYER).isActive(true).build());

        var roses   = cats.save(Category.builder()
                .name("Roses").displayOrder(1).build());
        var lilies  = cats.save(Category.builder()
                .name("Lilies").displayOrder(2).build());
        var orchids = cats.save(Category.builder()
                .name("Orchids").displayOrder(3).build());
        var sunny   = cats.save(Category.builder()
                .name("Sunflowers").displayOrder(4).build());
        var carns   = cats.save(Category.builder()
                .name("Carnations").displayOrder(5).build());

        products.saveAll(List.of(
                p("Red Rose Freedom","12.50",100,5000,
                        "https://images.unsplash.com/photo-1560717789-0ac7c58ac90a?w=600",
                        "Red","Kenya",60,true,roses),
                p("Pink Avalanche Rose","14.00",50,3000,
                        "https://images.unsplash.com/photo-1518895949257-7621c3c786d7?w=600",
                        "Pink","Colombia",70,true,roses),
                p("White Vendela Rose","11.00",100,4200,
                        "https://images.unsplash.com/photo-1589798878730-d24e4aae7d96?w=600",
                        "White","Ecuador",60,false,roses),
                p("Orange Rose Voodoo","13.50",50,2000,
                        "https://images.unsplash.com/photo-1518895949257-7621c3c786d7?w=600",
                        "Orange","Kenya",65,false,roses),
                p("Stargazer Lily","18.00",50,2000,
                        "https://images.unsplash.com/photo-1490750967868-88df5691cc9a?w=600",
                        "Pink","Netherlands",80,true,lilies),
                p("White Asiatic Lily","15.00",50,1500,
                        "https://images.unsplash.com/photo-1599684584505-26a2d7a8a779?w=600",
                        "White","Netherlands",75,false,lilies),
                p("Purple Dendrobium","25.00",30,800,
                        "https://images.unsplash.com/photo-1566983274534-e17f3e7cbf22?w=600",
                        "Purple","Thailand",65,true,orchids),
                p("White Phalaenopsis","35.00",20,500,
                        "https://images.unsplash.com/photo-1566983274534-e17f3e7cbf22?w=600",
                        "White","Taiwan",70,true,orchids),
                p("Giant Sunflower","8.50",50,3500,
                        "https://images.unsplash.com/photo-1597848212624-a19eb35e2651?w=600",
                        "Yellow","India",80,false,sunny),
                p("Teddy Bear Sunflower","7.00",100,4500,
                        "https://images.unsplash.com/photo-1508193638397-1c4234db14d8?w=600",
                        "Yellow","India",55,false,sunny),
                p("Red Carnation","5.50",200,8000,
                        "https://images.unsplash.com/photo-1490750967868-88df5691cc9a?w=600",
                        "Red","Colombia",55,false,carns),
                p("Spray Carnation Mixed","6.00",100,6000,
                        "https://images.unsplash.com/photo-1490750967868-88df5691cc9a?w=600",
                        "Mixed","Kenya",50,false,carns)
        ));

        System.out.println(
                "\n✅ Blooms database seeded successfully!");
        System.out.println(
                "   Admin : admin@blooms.com / Admin@123");
        System.out.println(
                "   Buyer : buyer@blooms.com / Buyer@123\n");
    }

    private Product p(
            String name, String price,
            int moq, int stock, String img,
            String color, String origin, int stem,
            boolean featured, Category cat) {
        return Product.builder()
                .name(name)
                .pricePerStem(new BigDecimal(price))
                .minOrderQuantity(moq)
                .stockQuantity(stock)
                .imageUrl(img)
                .color(color)
                .originCountry(origin)
                .stemLengthCm(stem)
                .isFeatured(featured)
                .isActive(true)
                .category(cat)
                .build();
    }
}