package chapter.one;

interface RunnableEx {
    void run() throws Exception;

    static Runnable uncheck(final RunnableEx runner) {
            return () -> {
                try {
                    runner.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
    }
}
