/*
 * Copyright (C) 2025 jsnor.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package inside.dumpster.database;

import com.github.jaokim.arguably.Arguments;

/**
 *
 * @author jsnor
 */
public class DatabaseGeneratorArguments extends Arguments {
  public final Arg ConnectionString = new Arguments.Arg("-connectionstring", 
          "connection", 
          String.class, 
          Arguments.Arg.Is.Optional, 
          "JDBC connection string, f.i. jdbc:derby://localhost:1527/dumpster", 
          "jdbc:derby:dumpster", 
          Arg.Askable.Yes);
      public final static DatabaseGeneratorArguments Instance;
    static {
      DatabaseGeneratorArguments inst = null;
      try {
        inst = new DatabaseGeneratorArguments(new String[0]);
      } catch (Exception ex) {
      } finally {
        Instance = inst;
      }
    }

  public DatabaseGeneratorArguments(String[] string) throws Exception {
    super.parseArgs(string);
  }
}
