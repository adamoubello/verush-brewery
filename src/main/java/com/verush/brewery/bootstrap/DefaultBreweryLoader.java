package com.verush.brewery.bootstrap;

import com.verush.brewery.domain.*;
import com.verush.brewery.domain.security.Authority;
import com.verush.brewery.domain.security.User;
import com.verush.brewery.repositories.*;
import com.verush.brewery.repositories.security.AuthorityRepository;
import com.verush.brewery.repositories.security.UserRepository;
import com.verush.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        loadBreweryData();
        loadCustomerData();
    }

    private void loadUserData() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);

        User user = User.builder()
                .id(1)
                .username("user")
                .password(bcrypt.encode("user"))
                .authority(authorityRepository.getOne(1))
                .build();
        User userScott = User.builder()
                .id(1)
                .username("pascal")
                .password(bcrypt.encode("siakam"))
                .authority(authorityRepository.getOne(2))
                .build();
        User userSpring = User.builder()
                .id(1)
                .username("verush")
                .password(bcrypt.encode("verush"))
                .authority(authorityRepository.getOne(3))
                .build();

        userRepository.save(user);
        userRepository.save(userScott);
        userRepository.save(userSpring);
    }

    private void loadAuthoritiesData() {
        Authority authorityUser = Authority.builder()
                .id(1)
                .role("USER")
                //.users(Set.of(User.builder().username("").password("").build()))
                .build();
        Authority authorityCustomer = Authority.builder()
                .id(2)
                .role("CUSTOMER")
                .build();
        Authority authorityAdmin = Authority.builder()
                .id(3)
                .role("ADMIN")
                .build();

        authorityRepository.save(authorityUser);
        authorityRepository.save(authorityCustomer);
        authorityRepository.save(authorityAdmin);
    }

    private void loadCustomerData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Verush Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }
}
