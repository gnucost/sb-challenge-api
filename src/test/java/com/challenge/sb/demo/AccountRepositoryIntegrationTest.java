package com.challenge.sb.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void WhenFindByName(){
        // given
        Account tony = new Account("Tony Stark", "Checking account");
        entityManager.persist(tony);
        entityManager.flush();

        // when
        Account found = accountRepository.findByName(tony.getName());

        // then
        assertThat(found.getName()).isEqualTo(tony.getName());
    }
}
