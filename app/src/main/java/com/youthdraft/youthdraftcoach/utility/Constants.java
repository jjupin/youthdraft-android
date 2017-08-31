package com.youthdraft.youthdraftcoach.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjupin on 11/21/16.
 */

public class Constants {

    public static final boolean debug = true;

    public static final Map<String, String> coachesMap;
    static {
        Map<String, String> cMap = new HashMap<String, String>();
        cMap.put("ae6172b7-571d-4135-892f-e753feb969ea", "coach.gamma@yothdraft.com");
        cMap.put("fafec932-a179-4f2f-9e02-38d0ab51717a", "coach.beta@youthdraft.com");
        cMap.put("4496e8e6-f722-470c-939c-09c084c7c345", "coach.delta@youthdraft.com");
        cMap.put("2be89491-65c3-4d47-a477-f4e93da66265", "coach.zeta@youthdraft.com");
        cMap.put("183dba1d-41e3-4911-9fe8-33ace8243669", "coach.alpha@youthdraft.com");

        coachesMap = Collections.unmodifiableMap(cMap);
    };
}
