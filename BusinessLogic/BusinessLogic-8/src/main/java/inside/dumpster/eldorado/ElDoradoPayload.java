/*
 * Copyright 2022 JSNORDST.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package inside.dumpster.eldorado;

import inside.dumpster.client.Payload;

/**

 * 
 * @author JSNORDST
 */
public class ElDoradoPayload extends Payload {

    public int getGold() {
        return this.getDstBytes();
    }
    
    public void setGold(int gold) {
        this.setDstBytes(gold);
    }
    
}
