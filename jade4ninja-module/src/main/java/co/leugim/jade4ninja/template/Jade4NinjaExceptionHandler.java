/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.leugim.jade4ninja.template;

import java.io.PrintWriter;
import java.io.Writer;

import ninja.utils.NinjaProperties;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Jade4NinjaExceptionHandler {

    @Inject
    private Logger logger;
    @Inject
    private NinjaProperties ninjaProperties;

    public void handleException(Writer out, Exception e) {

        PrintWriter pw = (out instanceof PrintWriter) ? (PrintWriter) out
                : new PrintWriter(out);
        pw.println("<!-- JADE ERROR MESSAGE STARTS HERE -->"
                + "<script language=javascript>//\"></script>"
                + "<script language=javascript>//\'></script>"
                + "<script language=javascript>//\"></script>"
                + "<script language=javascript>//\'></script>"
                + "</title></xmp></script></noscript></style></object>"
                + "</head></pre></table>"
                + "</form></table></table></table></a></u></i></b>"
                + "<div align=left "
                + "style='background-color:#FFFF00; color:#FF0000; "
                + "display:block; border-top:double; padding:2pt; "
                + "font-size:medium; font-family:Arial,sans-serif; "
                + "font-style: normal; font-variant: normal; "
                + "font-weight: normal; text-decoration: none; "
                + "text-transform: none'>"
                + "<b style='font-size:medium'>Jade template error!</b>");
        if (!ninjaProperties.isProd()) {
            pw.println("<pre><xmp>");
            e.printStackTrace(pw);
            pw.println("</xmp></pre>");
        }
        pw.println("</div></html>");
        pw.flush();
        pw.close();

        logger.error("Templating error.", e);

    }
}