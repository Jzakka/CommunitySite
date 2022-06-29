package CommunitySIte.demo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HelloRepositoryTest {

    @Autowired
    HelloRepository helloRepository;

    @Test
    public void save(){
        Hello hello = new Hello();
        hello.setName("TEST");
        Long saveId = helloRepository.save(hello);

        Assertions.assertThat(hello.getId()).isEqualTo(saveId);
    }

    @Test
    public void findOne(){
        Hello hello = new Hello();
        hello.setName("hello");

        helloRepository.save(hello);

        Hello findHello = helloRepository.findOne(hello.getId());
        Assertions.assertThat(findHello).isEqualTo(hello);
    }

    @Test
    public void findAll() {
        Hello hello1 = new Hello();
        Hello hello2 = new Hello();

        helloRepository.save(hello1);
        helloRepository.save(hello2);

        List<Hello> result = helloRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}