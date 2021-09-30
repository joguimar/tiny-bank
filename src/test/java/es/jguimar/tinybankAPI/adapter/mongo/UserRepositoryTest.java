package es.jguimar.tinybankAPI.adapter.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import es.jguimar.tinybankAPI.adapter.mongo.UserMongoRepository;
import es.jguimar.tinybankAPI.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

  public static final String NAME_SAVED = "name saved";

  @Autowired
  UserMongoRepository userMongoRepository;

  @Autowired
  MongoTemplate mongoTemplate;


  @Test
  public void findByName_shouldReturnOK() {
    // given
    DBObject objectToSave = BasicDBObjectBuilder.start().add("name", NAME_SAVED).get();

    // when
    mongoTemplate.save(objectToSave, "user");

    Optional<User> safebox = userMongoRepository.findByName(NAME_SAVED);

    // then
    assertThat(safebox.get().getName()).isEqualTo(NAME_SAVED);
  }
}