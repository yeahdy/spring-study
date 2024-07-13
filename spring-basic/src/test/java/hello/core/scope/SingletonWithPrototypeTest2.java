package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import hello.core.scope.SingletonWithPrototypeTest1.PrototypeBean;
import jakarta.inject.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest2 {
    @Test
    @DisplayName("싱글톤 빈 내부에 ObjectProvider는 항상 새로운 프로토타입 빈을 반환하기 때문에 count는 같다")
    void singletonClientUseObjectProvider() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean2.class, PrototypeBean.class);
        ClientBean2 clientBean = ac.getBean(ClientBean2.class);

        int prototypeCount1 = clientBean.logic();   //프로토타입 빈 @x01
        int prototypeCount2 = clientBean.logic();   //프로토타입 빈 @x02

        assertThat(prototypeCount1).isEqualTo(prototypeCount2);
    }

    @Test
    @DisplayName("싱글톤 빈 내부에 Provider는 항상 새로운 프로토타입 빈을 반환하기 때문에 count는 같다")
    void singletonClientUseProvider() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean3.class, PrototypeBean.class);
        ClientBean3 clientBean = ac.getBean(ClientBean3.class);

        int prototypeCount1 = clientBean.logic();   //프로토타입 빈 @x01
        int prototypeCount2 = clientBean.logic();   //프로토타입 빈 @x02

        assertThat(prototypeCount1).isEqualTo(prototypeCount2);
    }

    @Scope("singleton")
    static class ClientBean2 {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;    //스프링이 제공하는 기능이지만, 스프링에서 자동으로 주입하기 때문에
                                                                        //단위테스트나 mocking 테스트 가능
        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();    //스프링 컨테이너에 요청 후 생성되었을 때 빈을 찾아서 반환
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    static class ClientBean3 {
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;  //자바 표준으로 필요한 정도의 DL(Depedency Lookup)을 제공
                                                                //단위테스트나 mocking 테스트 훨씬 쉬워짐
        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();    //스프링 컨테이너에 요청 후 생성되었을 때 빈을 찾아서 반환
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}
