package com.Yesphet.jweb;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yesphet on 17/1/12.
 */
public class JWebFilter implements Filter {

    private static final LogTimer logTask = new LogTimer();
    private static final Timer timer = new Timer(true);
    final static Logger log = Logger.getLogger(JWebFilter.class);

    private static AtomicInteger threads;
    private int sleepTime;

    public void init(FilterConfig filterConfig) throws ServletException {
        threads = new AtomicInteger(0);
        timer.schedule(logTask,0,1000);

        Properties properties = new Properties();
        try {
            InputStream inputStream = JWebFilter.class.getResourceAsStream("/setting.properties");
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            //忽略
        }
        sleepTime = Integer.parseInt(properties.getProperty("SleepTime"));

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        threads.incrementAndGet();
        if (sleepTime > 0){
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        ((HttpServletResponse)servletResponse).setStatus(200);
        threads.decrementAndGet();
    }

    public void destroy() {

    }

    private static class LogTimer extends TimerTask{

        public void run() {
            log.info("CurrentThreads:"+threads.get());
        }
    }

}
