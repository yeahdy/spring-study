package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonTest {

    @Test
    void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        System.out.println("========FOUND BEAN1========");
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        System.out.println("SingletonBean1 = "+singletonBean1);
        System.out.println("SingletonBean2 = "+singletonBean2);
        assertThat(singletonBean1).isSameAs(singletonBean2);    //스프링 빈은 싱글톤이기 때문에 스프링 컨테이너는 항상 같은 빈을 반환한다.
        ac.close();
    }

    @Scope
    static class SingletonBean {
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }
}
