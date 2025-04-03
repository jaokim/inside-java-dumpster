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

import inside.dumpster.bl.BusinessLogicException;
import inside.dumpster.bl.BusinessLogicService;
import inside.dumpster.client.Result;

/**
 * El Dorado, queen of cities Overflowing with excess Every turn exotic, lavish
 * Precious wonders numberless Lead me now into temptation Surely you would not
 * deny One who's come so far to find you All your blessings save goodbye
 *
 * @author JSNORDST
 */
public class ElDoradoService extends BusinessLogicService<ElDoradoPayload, Result> {

  private int collectedGold = 0;

  //public ElDoradoService(Class<ElDoradoPayload> payloadClass, Class<Result> resultClass) {
  //  super(payloadClass, resultClass);
  //}
  public ElDoradoService() {
    super(ElDoradoPayload.class, Result.class);
  }

  private int dropGold(int iteration) {
    if (iteration == 0) {
      return collectedGold;
    } else {
      addGoldToPond();
      iteration--;
      return dropGold(iteration);
    }
  }

  private void addGoldToPond() {
    collectedGold++;
  }

  @Override
  public Result invoke(ElDoradoPayload payload) throws BusinessLogicException {
    try {
      this.dropGold(payload.getGold());
    } catch (StackOverflowError soe) {
      // jsut ignore
    }
    Result res = new Result();
    res.setResult(String.format("%d", collectedGold));
    return res;
  }
}
