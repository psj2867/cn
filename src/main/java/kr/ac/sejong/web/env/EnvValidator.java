package kr.ac.sejong.web.env;

import kr.ac.sejong.util.CommonUtil;
import kr.ac.sejong.web.exception.EnvException;

@FunctionalInterface
public interface EnvValidator{
    void valid(String s);

    static EnvValidator Idenfiy = (s) -> {};
    static EnvValidator NotNull(String type) {
        return (s) -> {
            if (CommonUtil.isEmpty(s)) {
                throw new EnvException( type + " required not null");
            }
        };
    }
}
