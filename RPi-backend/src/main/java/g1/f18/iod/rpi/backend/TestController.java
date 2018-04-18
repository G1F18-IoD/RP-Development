/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author chris
 */
@RestController
public class TestController {

    private static final String template = "fuck you daniel. %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/test")
    public Test greeting(@RequestParam(value="name", defaultValue="You bloody cunt") String name) {
        return new Test(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
