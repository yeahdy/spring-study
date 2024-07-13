package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    @DisplayName("프로토타입 빈은 항상 스프링 컨테이너에 새로운 요청을 한다.")
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("========FOUND BEAN========");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("싱글톤 빈 내부에 프로토타입 빈이 있으면 프로토타입 빈은 싱글톤 빈 생성 시점에 주입되고, 이후 스프링 컨테이너에 다시 요청하지 않는다.")
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    @Test
    @DisplayName("싱글톤 빈 내부에 프로토타입 빈을 같이 사용하면 반환하는 프로토타입 빈은 같다")
    void singletonClientUseSamePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        PrototypeBean prototypeBean1 = clientBean1.getPrototypeBean();

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        PrototypeBean prototypeBean2 = clientBean2.getPrototypeBean();

        assertThat(prototypeBean1).isEqualTo(prototypeBean2);
    }

    @Scope("singleton")
    static class ClientBean {
        private final PrototypeBean prototypeBean;  //ClientBean 생성 시점에 주입됨

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {    //스프링 빈에 등록되면서 의존성 주입 시 프로토타입에 주입 요청
            this.prototypeBean = prototypeBean;
        }

        public PrototypeBean getPrototypeBean() {
            return prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }

    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
