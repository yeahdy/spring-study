package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {

    @Test
    void PrototypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("========FIND BEAN1========");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("========FIND BEAN2========");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = "+prototypeBean1);
        System.out.println("prototypeBean2 = "+prototypeBean2);
        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);    //스프링 빈은 싱글톤이기 때문에 스프링 컨테이너는 항상 같은 빈을 반환한다.

        // 프로토타입 스코프의 빈은 스프링 컨테이너 종료 시점에 빈을 관리하지 않기 때문에 직접 빈 종료 메소드를 호출한다.
        prototypeBean1.destroy();
        prototypeBean2.destroy();
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }

}
