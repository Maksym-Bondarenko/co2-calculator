package org.challange.cli;

import java.util.*;

public final class ArgumentParser {

    private ArgumentParser() {
    }

    public static CLIOptions parse(String[] args) {

        /* Option 1: 3 parameters given in CLI */
        if (args.length == 3 && !args[0].startsWith("--")) {
            return new CLIOptions(args[0], args[1], args[2]);
        }
        /* Option 2: named parameters given in any order either with '=' or space between key-value pairs */

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String token = args[i];

            if (!token.startsWith("--")) {
                throw new IllegalArgumentException(
                        "Unexpected token: " + token + " (flags must start with \"--\")");
            }

            /* style 2a: --key=value  (all in one arg, equal-sign '=') */
            int eq = token.indexOf('=');
            if (eq > 0) {
                String key = token.substring(0, eq);
                String val = token.substring(eq + 1);
                if (val.isEmpty()) {
                    throw new IllegalArgumentException("Missing value after '=' for " + key);
                }
                map.put(key, val);
                continue;
            }

            /* style 2b: --key value  (SPACE: needs next arg) */
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("Missing value after " + token);
            }
            map.put(token, args[++i]);   // value comes as next argument
        }

        /* required flags */
        String car = map.get("--transportation-method");
        String start = map.get("--start");
        String end = map.get("--end");

        if (car == null || start == null || end == null) {
            throw new IllegalArgumentException("""
                    Required arguments (any order, with space OR =):
                      --transportation-method <type>   or  --transportation-method=<type>
                      --start <city>   or  --start=<city>
                      --end <city>     or  --end=<city>
                    """);
        }
        return new CLIOptions(car, start, end);
    }
}
