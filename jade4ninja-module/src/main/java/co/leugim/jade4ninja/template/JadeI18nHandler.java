package co.leugim.jade4ninja.template;

import ninja.Context;
import ninja.Result;
import ninja.i18n.Messages;

import com.google.common.base.Optional;

public class JadeI18nHandler {

    private Messages messages;
    private Context context;
    private Optional<Result> result;

    public JadeI18nHandler(Messages messages, Context context, Result result) {
        this.messages = messages;
        this.context = context;
        this.result = Optional.of(result);
    }

    public String getValue(String key, String... args){
        if (key == null || key.isEmpty()) {

            throw new RuntimeException("Using i18n without any key is not possible.");
        }
            Optional<String> optResult = messages.get(key, context, result, args);
            if(optResult.isPresent())
                return optResult.get();
            else
                return key;

    }
}
