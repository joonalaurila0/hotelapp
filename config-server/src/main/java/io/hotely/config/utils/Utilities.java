package io.hotely.config.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.DefaultPropertiesPersister;
import org.yaml.snakeyaml.Yaml;

public class Utilities {

 /**
 * Returns list of files from the directory path as a set of strings
 */
  public Set<String> fileList(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream
        .filter(file -> !Files.isDirectory(file))
        .map(Path::getFileName)
        .map(Path::toString)
        .collect(Collectors.toSet());
    }
  }

   /**
 * Returns current working directory 
 */
  public String cwd() {
    String userDir = Paths.get("")
      .toAbsolutePath()
      .toString();
    return userDir;
  }

   /**
 * Returns the file as a single string
 */
 public String readFileAsString(String filePath) throws IOException {
    InputStream inputStream = new FileInputStream(filePath);
    int ch;
    String file = "";
    // while inputStream can read the next byte from input stream, loop
    while ((ch = inputStream.read()) != -1) {
      file = file + (char)ch;
    }
    inputStream.close();
    return file;
  }

  /**
 * Returns the file as tokens in a List
 */
  public List<Object> readFileAsTokens(String filePath) throws IOException {
      FileReader reader = new FileReader(filePath);
      StreamTokenizer tokenizer = new StreamTokenizer(reader);
			List<Object> tokens = new ArrayList<Object>();

			int currentToken = tokenizer.nextToken();
			while (currentToken != StreamTokenizer.TT_EOF) {
					if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
							tokens.add(tokenizer.nval);
					} else if (tokenizer.ttype == StreamTokenizer.TT_WORD
							|| tokenizer.ttype == '\''
							|| tokenizer.ttype == '"') {
							tokens.add(tokenizer.sval);
					} else {
							tokens.add((char) currentToken);
					}
					currentToken = tokenizer.nextToken();
			}
			return tokens;
		}

  /**
 * Returns YAML file as a map of strings
 */
  public Map<String, Object> getFileMap(String filePath) throws IOException {
    InputStream inputStream = new FileInputStream(filePath);
    Yaml yaml = new Yaml();
    Map<String, Object> data = yaml.load(inputStream);
    return data;
  }

 /**
 * Writes to a file determined by file path, replaces text and writes it
 *
 * @param filePath      The absolute file path to the file
 * @param toReplace     The text to replace in the file
 * @param replacedWith  The text that gets written over to replace old text
 */
  public void replaceFromFile(String filePath, String toReplace, String replacedWith) throws IOException {
    File file = new File(filePath);
    String content = "";
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = reader.readLine();
    while (line != null) {
      content = content + line + System.lineSeparator();
      line = reader.readLine();
    }
    String newContent = content.replaceAll(toReplace, replacedWith);
    FileWriter writer = new FileWriter(file);
    writer.write(newContent);
    reader.close();
    writer.close();
  }

  /**
 * Returns the root token for HashiCorp Vault from token.yml
 */
  public String getRootToken() throws IOException {
      String file = this.cwd() + "/token.yml";
      InputStream inputStream = new FileInputStream(file);
      Yaml yaml = new Yaml();
      Map<String, Object> data = yaml.load(inputStream);
      Object tokenObj = data.get("spring.cloud.config.server.vault.token");
      String token = tokenObj.toString();
      return token;
  }

  /**
   * Writes over application properties with given value.
   * @throws IOException
   */
  public void writeConfig() throws IOException {
    Utilities utils = new Utilities();
    String token = utils.getRootToken();

    Properties props = new Properties();
    props.setProperty("hunaja", token);
    File app = new File(utils.cwd() + "/src/main/resources/application.yml");
    OutputStream out = new FileOutputStream(app);
    DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
    persister.store(props, out, "Header Comment");
  }
}
