package com.job.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.commons.dto.request.maquette.Transaction.TransactionType;
import com.commons.dto.response.AccountBalances;
import com.commons.dto.response.BaseResponse;
import com.commons.dto.response.CustomResponse;
import com.commons.dto.response.CustomResponse.Status;
import com.commons.dto.response.SsfbFailureResponse;
import com.commons.utility.CommonsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type. TypeReference;
        import com.fasterxml.jackson.databind. DeserializationFeature;
        import com.fasterxml.jackson.databind.JsonNode;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.google.common.hash.Hashing;
        import com.google.gson.Gson;
import com.job.sagar.constant.Constants;
import com.job.sagar.exception.BaseException;
import com.opencsv.CSVWriter;
        import com.opencsv.bean.StatefulBeanToCsv;
        import com.opencsv.bean.StatefulBeanToCsvBuilder;
        import com.opencsv.exceptions.CsvDataTypeMismatchException;
        import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
        import com.paytm.bank.constant.Constants;
        import com.paytm.bank.constant.ErrorCodesConstant;
        import com.paytm.bank.enums.Client;
        import com.paytm.bank.enums.HRAdmin;
        import com.paytm.bank.enums.OriginalTxnReportCodeEnum;
        import com.paytm.bank.enums.TransferModeEnum;
        import com.paytm.bank.exception.BaseException;
        import com.paytm.bank.extBank. CustomMappingStrategy;
        import com.paytm.bank.initializer.serviceerror.ServiceErrorFactory;
        import com.paytm.bank.model.transaction.db.CbsTransactionData;
import com.paytm.bank.model.transaction.db.FisTransactionData;
import com.paytm.bank.model.transaction.db.NodalData;
import com.paytm.bank.model.transaction.db.fundtransfer. FundTransferData;
import com.paytm.bank.model.transaction.db.fundtransfer.GratificationFundTransferData;
import com.paytm.bank.nodel.transaction.db.neft.GenericFundTransferData;
import com.paytm.bank.objects.request.CheckLimitRequest;
import com.paytm.bank.objects.request.FundTransferRequest;
import com.paytm.bank.objects.request. InitRequest;
import com.paytm.bank.service.Service;
import com.paytm.bank.service.neft.FundMovementServiceFactory;
import lombok.NonNull;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction. CannotCreateTransactionException;
import org.springframework.transaction. TransactionSystemException;
import org.springframework.web.client.ResourceAccessException;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net. InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.job.sagar.constant.Constants.AUTHORIZATION;
import static com.job.sagar.constant.Constants.FAILED;
import static com.job.sagar.constant.Constants.JWT_TOKEN_GENERATION_EXCEPTION_MESSAGE;
import static com.job.sagar.constant.Constants.UTILS_INSTANTIATION;
import static com.job.sagar.constant.ErrorCodesConstant.*;
import static com.paytm.bank.constant.Constants.AUTHORIZATION;
import static com.paytm.bank.constant.Constants.BATCHOPS_SECRET_KEY;
import static com.paytm.bank.constant.Constants.CASA_FLAG;
import static com.paytm.bank.constant.Constants.CBS_PROCESSED_DATE;
import static com.paytm.bank.constant.Constants.CNBS_SECRET_KEY;
import static com.paytm.bank.constant.Constants.FAILED;
import static com.paytm.bank.constant.Constants.FIS;
import static com.paytm.bank.constant.Constants.IMPS_TRANSACTION_TYPE;
import static com.paytm.bank.constant.Constants.IS_CUSTOM_PMT_ADD;
import static com.paytm.bank.constant.Constants. IVR_SECRET_KEY;
import static com.paytm.bank.constant.Constants.JWTTOKEN;
        import static com.paytm.bank.constant.Constants. JWT_TOKEN_GENERATION_EXCEPTION_MESSAGE;
        import static com.paytm.bank.constant.Constants.OAUTH;
        import static com.paytm.bank.constant.Constants. ONBOARD_DATE;
        import static com.paytm.bank.constant.Constants.PRODUCT_SECRET_KEY;
        import static com.paytm.bank.constant.Constants.REQUESTTOKEN;
        import static com.paytm.bank.constant.Constants.RRN;
        import static com.paytm.bank.constant.Constants.SI_SECRET_KEY;
        import static com.paytm.bank.constant.Constants.SUBSCRIPTION_FROM_DATE;
        import static com.paytm.bank.constant.Constants.SUBSCRIPTION_TO_DATE;
        import static com.paytm.bank.constant.Constants. TRANSACTION_POSTING_TIMESTAMP;
        import static com.paytm.bank.constant.Constants. TRANSACTION_TYPE;
        import static com.paytm.bank.constant.Constants. TRANSFER_MODE;
        import static com.paytm.bank.constant.Constants. TRANSFER_MODE_NEFT;
        import static com.paytm.bank.constant.Constants. TRANSFER_MODE_RTGS;
        import static com.paytm.bank.constant.Constants.TSERVICE;
        import static com.paytm.bank.constant.Constants.TYPE_STS; I
        import static com.paytm.bank.constant.Constants.USERTOKEN;
        import static com.paytm.bank.constant.Constants.UTILS_INSTANTIATION;
        import static com.paytm.bank.constant.Constants.UTR;
        import static com.paytm.bank.constant. Constants.VALIDATION_TOKEN;
        import static com.paytm.bank.constant.ErrorCodesConstant.DATABASE_SERVICE_UNAVAILABLE;
        import static com.paytm.bank.constant.ErrorCodesConstant.DATE_PARSE_ERROR;
        import static com.paytm.bank.constant.ErrorCodesConstant.INTERNAL_SERVER_ERROR;
import static java.util.Calendar.*;

public class CommonUtils {

    public static String ssfoDebitPending;
    public static String ssfoDebitFailure = null;

    public static String ssfbCreditPending = null;
    public static String ssfbCreditFailure = null;
    public static String cbsDebitPending = null;
    public static String cbsDebitFailure = null;

    public static String ssfbDebitStatusPending = null;
    public static String ssfbCreditStatusPending = null;

    public static String ssfbDebitStatusFailure = null;
    public static String ssfbCreditStatusFailure = null;
    public static String masterUpdationCreditPending = null;
    public static String masterUpdationDebitPending = null;
    public static String cifCreationPending = null;
    public static String cifCreationFailure = null;
    private static List<String> httpErrorCodes = new ArrayList<>(Arrays.asList("500", "582", "583", "504"));
    public static List<String> restrictedHrAdminReportCodes = Arrays.asList("90100", "98110", "20261", "90120");
    public static List<Integer> errorCodesToSkipStackTrace = Arrays.asList(1136, 8003, 1054, 1118);
    public static final SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N',
            '0', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);

    private static final String REQUEST_ID = "requestId";
    static Map<String, String> transactionType = new HashMap<>();
    static int count;


    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static Gson gson = new Gson();
    private static String regex = "\\d{12}";
    private static Pattern pattern = Pattern.compile(regex);
    private static final Pattern mobileNumberPattern = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern amountPattern = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
    public static Set<String> impsOutwardSet = new HashSet<>();
    public static Set<String> impsInwardSet = new HashSet<>();
    public static Set<String> neftOutwardSet = new HashSet<>();
    public static Set<String> neftInwardSet = new HashSet<>();
    public static Set<String> rtgsOutwardSet = new HashSet<>();

    public static Set<String> rtgsInwardSet = new HashSet<>();
    public static Set<String> netbankingSet = new HashSet<>();
    private static Set<String> debitCardEcomSet = new HashSet<>();
    private static Set<String> debitCardPosSet = new HashSet<>();
    private static Set<String> debitCardAtmSet = new HashSet<>();
    private static Set<String> debitCardEcomVisaDomSet = new HashSet<>();
    private static Set<String> debitCardEcomVisaIntSet = new HashSet<>();
    private static Set<String> debitCardPosVisaDomSet = new HashSet<>();
    private static Set<String> debitCardPosVisaIntSet = new HashSet<>();
    private static Set<String> debitCardAtaVisaDomSet = new HashSet<>();
    private static Set<String> debitCardAtVisaIntSet = new HashSet<>();
    private static Set<String> debitCardEcomMcDomSet = new HashSet<>();
    private static Set<String> debitCardEcomMcIntSet = new HashSet<>();
    private static Set<String> debitCardPosMcDomSet = new HashSet<>();
    private static Set<String> debitCardPosMcIntSet = new HashSet<>();
    private static Set<String> debitCardAtmMcDomSet = new HashSet<>();
    private static Set<String> debitCardAtmMcIntSet = new HashSet<>();

    private CommonUtils() {
        throw new NotImplementedException(UTILS_INSTANTIATION);
    }

    private static String generateYearPart(int year) {
        year = year - 2000;
        int a = year % 36;
        int b = (year / 36) % 36;
        return "" + digits[b] + digits[a];
    }

    private static String toUnsignedString0(int val, int shift) {
        int mag = Integer.SIZE - Integer.numberOfLeadingZeros(val);
        int chars = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[chars];
        formatUnsignedInt(val, shift, buf, 0, chars);
        return new String(buf);
    }

    static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = digits[val & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);
        return charPos;
    }

    public static String fileToString(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            Files.lines(file.toPath()).forEach(builder::append);
        } catch (IOException e) {
            logger.error("Exception occured while converting file to string: {}",
                    CommonsUtility.exceptionFormatter(e));
        }
        return builder.toString();
    }

    public static long convertMysqlDate(String date) {
        try {
            return mysqlDateFormat.parse(date).getTime();
        } catch (ParseException pe) {
            throw new BaseException(FAILED, INTERNAL_SERVER_ERROR_CODE, Constants.INTERNAL_SERVER_ERROR);
        }
    }

    public static String datetimeToString(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String maskNumber(String number, String mask) {
        if (StringUtils.isBlank(number) || StringUtils.isBlank(mask) || number.length() != mask
                .length()) {
            return null;
        }
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(number.charAt(index));
                index++;
            } else if (c == 'X') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }
        return maskedNumber.toString();
    }

    public static String updatedMaskNumber(String number) {
        try {
            return number.substring(0, 2) + "XX" + number.substring(number.length() - 4);
        } catch (Exception e) {
            Logger.info("Exception occurred while masking account number: (), Exeption: ()", number,
                    CommonUtils.exceptionFormatter(e));
            return null;
        }
    }

    public static String maskNumber(String number) {
        if (number.length() < 4) {
            return "XXXXXX";
        } else {
            return "XXXXXX" + number.substring(number.length() - 4);
        }
    }

    public static String updatedMaskNumber(String number, int maskLength, int visibleCharsLength) {
        if (number.length() <= visibleCharsLength) {
            return number;
        } else {
            String mask = "";
            for (int i = 0; i < maskLength; i++) {
                mask += "X";
            }
            return mask + number.substring(number.length() - visibleCharsLength);
        }
    }

    public static String updatedMaskNumber(String number, int maskLength, int visibleCharsLength, String delimiter) {
        if (number.length() < visibleCharsLength) {
            return number;
        } else {
            String mask = "";
            for (int i = 0; i < maskLength; i++) {
                mask += "X";
            }
            return mask + delimiter + number.substring(number.length() - visibleCharsLength);
        }
    }

    public static String LastForDigit(String number) {
        if (number.length() < 4) {
            return "";
        } else {
            return number.substring(number.length() - 4);
        }
    }

    public static String getFormattedAccountNo(String accNo) {
        if (StringUtils.isNotBlank(accNo)) {
            if (accNo.length() > 4) {
                return accNo.substring(accNo.length() - 4);
            } else
                return accNo;
        }
        return "XXXX";
    }

    public static String getFormattedDate(@NonNull String dateFormatter, @NonNull Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatter);
        return simpleDateFormat.format(date);
    }

    public static String convertDateStringToEpoch(@NonNull String formatter, @NonNull String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatter);
            Date d = sdf.parse(dateString);
            Long epoch = d.getTime();
            logger.debug("Converted Date to epoch {}", String.valueOf(epoch));
            return String.valueOf(epoch);
        } catch (ParseException ex) {
            logger.error("Invalid date format, exception {}", CommonsUtility.exceptionFormatter(ex));
            throw new BaseException(FAILED, INTERNAL_SERVER_ERROR, "Invalid date format");
        }
    }

    public static String convertDateStringToEpochSec(@NonNull String formatter, @NonNull String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatter);
            Date d = sdf.parse(dateString);
            Long epoch = d.getTime() / 1000L;
            logger.debug("Converted Date to epoch ()", String.valueOf(epoch));
            return String.valueOf(epoch);
        } catch (ParseException ex) {
            logger.error("Invalid date format, exception ()", CommonsUtility.exceptionFormatter(ex));
            throw new BaseException(FAILED, INTERNAL_SERVER_ERROR, "Invalid date format");
        }
    }

    public static Long convertLocalDateTimeToEpoch(@NonNull LocalDateTime timestamp) {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String dateFormatter(@NonNull String oldDateFormatter, @NonNull String newDateFormatter, @NonNull String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldDateFormatter);
            Date date = simpleDateFormat.parse(dateString);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(newDateFormatter);
            String formattedDate = newDateFormat.format(date);
            return formattedDate;
        } catch (ParseException ex) {
            logger.error("Invalid date format, exception {} in converting from old format {} to new format {}",
                    CommonsUtility.exceptionFormatter(ex), oldDateFormatter, newDateFormatter);
            throw new BaseException(FAILED, INTERNAL_SERVER_ERROR, "Invalid date fornat");
        }
    }

    public static String jsonObjectToString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <U> U jsonStringToObject(String jsonString, Class<U> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public static <T> void createClaims(T request, Builder jwtBuilder) throws IOException {
        String requestJsonString = objectMapper.writeValueAsString(request);
        JsonNode jsonNode = objectMapper.readTree(requestJsonString);
        JsonNode node = objectMapper.readTree(jsonNode.toString());
        Iterator<Entry<String, JsonNode>> fieldsIterator = node.fields();
        while (fieldsIterator.hasNext()) {
            Entry<String, JsonNode> nodeEntry = fieldsIterator.next();
            String key = nodeEntry.getKey();
            JsonNode value = nodeEntry.getValue();
            if (StringUtils.isNotBlank(key)) {
                jwtBuilder.withClaim(key, value.textValue());
            }
        }
    }

    public static BaseException getExceptionForCode(String errorCode, String serviceName)
            throws BaseException {
        try {
            BaseException exception = ServiceErrorFactory.getException(serviceName, errorCode).orElse(ServiceErrorFactory
                    .getException(TSERVICE, String.valueOf(NO_ERROR_CODE_FOUND_IN_DB)).get());
            logger.error("Exception occurred in {} client. {}", serviceName, exception.toString());
            return exception;
        } catch (BaseException e) {
            logger.error("Internal server error in Client. {}", serviceName, e.toString());
            throw e;
        }
    }

    public static JsonNode convertStringToJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            logger.error("Exception occurred while parsing json string: {}, exception: {}", jsonString,
                    CommonsUtility.exceptionFormatter(e));
            throw new RuntimeException("Json parsing error");
        }
    }

    public static <T> T jsonToPojo(Object objectMap, Class<T> clazz) {
        return objectMapper.convertValue(objectMap, clazz);
    }

    public static <U> U convertJsonStringToMap(String jsonString, TypeReference typeRef) throws IOException {
        return (U) objectMapper.readValue(jsonString, typeRef);
    }

    public static <U> U convertJsonStringToMap(String jsonString, Type type) {
        return gson.fromJson(jsonString, type);
    }

    public static String convertObjectToJsonString(Object object) {
        return gson.toJson(object);
    }

    public static Map<String, Object> convertToStringToMap(String mapToString) {
        Map<String, Object> data = new HashMap<>();
        Pattern p = Pattern.compile("[\\{\\}\\=\\,_]+");
        String[] split = p.split(mapToString);
        for (int i = 1; 1 + 2 <= split.length; i += 2) {
            data.put(split[i], split[i + 1]);
        }
        return data;
    }

    public static String exceptionFormatter(Exception e) {
        StringBuilder sb = new StringBuilder(" | Exception: " + e.getClass());
        if (e.getCause() != null) {
            sb.append("| Cause: " + e.getCause());
        }
        if (e.getMessage() != null) {
            sb.append("| Exception message: " + e.getMessage().replaceAll("\"", ""));
        }
        if (checkIfPrintingTraceRequired(e)) {
            if (e.getStackTrace() != null) {
                sb.append("StackTrace: " + StringUtils.join(Arrays.asList(e.getStackTrace()).subList(0, Math.min(e.getStackTrace().length, 15)),
                        "----"));
            }
        }
        return sb.toString();
    }

    private static Boolean checkIfPrintingTraceRequired(Exception e) {
        if (e instanceof ResourceAccessException || (e instanceof BaseException && errorCodesToSkipStackTrace.contains(((BaseException) e).getCode()))) {
            return false;
        }
        return true;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String constructJwtTokenString(String issuer, String secret,
                                                 Map<String, String> claims) {
        try {
            JWTCreator.Builder jwtBuilder = JWT.create().withIssuer(issuer);
            claims.entrySet().forEach(claim -> jwtBuilder.withClaim(claim.getKey(), claim.getValue()));
            return jwtBuilder.sign(Algorithm.HMAC256(secret));
        } catch (Exception e) {
            logger.error("Exception: while generating jwt token ",
                    CommonsUtility.exceptionFormatter(e));
            throw new BaseException("failure", TOKEN_GENERATION_EXCEPTION,
                    JWT_TOKEN_GENERATION_EXCEPTION_MESSAGE);
        }
    }

    public static String getSchedulerRequestId(String prefix) {
        return "SCH" + (StringUtils.isNotEmpty(prefix) ? "-" + prefix
                : "") + "-" + LocalDateTime.now();
    }

    public static String convertDateToString(Date date, String format) {
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            logger.error("Error in converting date:{} to string", date);
            return null;
        }
    }

    public static Date convertStringToDate (String date, String format) {
        try {
            return new SimpleDateFormat (format).parse(date);
        } catch (Exception e) {
            logger.error( "Error in converting date: {} to string", date);
            return null;
        }
    }
    public static String formatDate(String inDate, String inputFormat, String outputFormat) {
        String outDate = "";
        if (inDate != null) {
            try {
                Date date = new SimpleDateFormat(inputFormat).parse(inDate);
                outDate = new SimpleDateFormat(outputFormat).format(date);
            } catch (Exception ex) {
                logger.error("Error in converting date: {} from inputFormat: {} to outputFormat", inDate,
                        inputFormat, outputFormat);
                return null;
            }
        }
        return outDate;
    }
    public static Date formatDatesForLong (Long date, String inputFormat, Long duration) {
        Long inDate = date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
            if (Objects.nonNull(duration)) {
                inDate = date * duration;
            }
            return sdf.parse(sdf.format(new Date(inDate)));
        } catch (Exception e) {
            logger.error("Error in converting long:) to date for inputFormat: ()", date, inputFormat);
            throw new BaseException("failure", DATE_PARSE_ERROR,
                    StringUtils.join("Invalid date format for date: ", date, "must be in format: ", inputFormat));
        }
    }
    public static Object getFieldUsingReflection (Object obj, String fieldName) {
        try {
            String methodName="get" + fieldName.substring(0,1).toUpperCase()+ fieldName.substring(1);
            Method getterMethod = obj.getClass().getMethod(methodName, new Class[]{});
            Object data = getterMethod.invoke(obj);
            return data;
        } catch (Exception e) {
            logger.error("Error while calling getter using reflection ", CommonUtils.exceptionFormatter(e));
            throw new BaseException("",12,"");
        }
    }
    public static String maskMobile (String number) {
        if (StringUtils.isEmpty(number))
            return number;
        if (number.length()> 4) {
            return "XXXXX" + number.substring(number.length() - 5);
        }
        else {
            return "XXXXX";
        }
    }
    public static Date convertToDate(String format, String date) {
        try {
            return new SimpleDateFormat (format).parse(date);
        } catch (ParseException e) {
            logger.error("Error in parsing file");
            return null;
        }
    }

    public static String convertToDateFormat(String format, String date) {
        try {
            SimpleDateFormat changeFormat = new SimpleDateFormat (format);
            return changeFormat.format(changeFormat.parse(date));
        } catch (ParseException e) {
            logger.error(  "Error in converting to date format. error :[{}]", CommonsUtility.exceptionFormatter (e));
            return null;
        }
    }

    public static List<String> getEnumNamesList (Class<? extends Enum<?>> e) {
        return Arrays.asList(getEnumNamesArray(e));
    }

    public static String[] getEnumNamesArray (Class<? extends Enum<?>> e) {
        String[] enumArray = Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        return enumArray;
    }
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    public static <T> void saveRowsToCSVFile (List<T> rows, String filePath, Class<T> clazz) {
        logger.debug("Starting csv creation for filePath {}", filePath);
        try {
            new File(filePath).getParentFile().mkdirs();
            CustomMappingStrategy<T> mappingStrategy = new CustonMappingStrategy<>();
            mappingStrategy.setType(clazz);
            Writer writer = new FileWriter(filePath, false);
            StatefulBeanToCsv<T> statefulBeanToCsv = new StatefulBeanToCsvBuilder<T>(writer).
                    withMappingStrategy(mappingStrategy).withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
            statefulBeanToCsv.write(rows);
            writer.close();
        }
        catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | RuntimeException e) {
                logger.error("Error occurred while writing into csv {} for file path", CommonUtils.exceptionFormatter(e), filePath);
                throw new RuntimeException("CSV generation failed for reason: " + e.getMessage());
            }
        }

        public static String createSHA256Hash(final String text) {
            String sha256hex = Hashing.sha256().hashString(text, StandardCharsets.UTF_8).toString();
            return sha256hex;
        }

    public static void updateField(Object object, String fieldName, String value)
    throws Exception {
        Method setterMethod = object.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), String.class);
        setterMethod.invoke(object, value);
    }
    public static Object getFieldData (Object object, String dataField) {
        try {
            String methodName="get" + dataField.substring(0, 1).toUpperCase()+ dataField.substring(1);
            Method getterMethod = object.getClass().getMethod(methodName, new Class[]{});
            return getterMethod.invoke(object);
        } catch (Exception e) {
            logger.error( "error while fetching dataField {} from reflection.", dataField);
        }
        return null;
    }

    public static Long convertMillisecCbsDateToSecFormat(String date) { return Long.parseLong (date) / 1000; }
    public static Map<String, String> getPropertiesUsingColun(String extraInfo) {
        Map<String, String> properties = new HashMap<>();
        if (StringUtils.isNotEmpty(extraInfo)) {
            String value = extraInfo;
            value = value.substring(1, value.length() - 1);
            String[] keyValuePairs = value.split("-");
            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":");
                if (entry != null && entry.length == 2) {
                    properties.put(entry[0].trim(), entry[1].trim());
                }
            }
        }
        return properties;
    }

    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress().toLowerCase();
        } catch (UnknownHostException e) {
            logger.error("Unable to get Ip Address");
        }
        return "127.0.0.1";
    }

    public static boolean isValidMobile(String mobile) {
        if (StringUtils.isBlank (mobile)) {
            logger.debug("invalid mobile number :)", mobile);
            return false;
        }
        Matcher matcher = mobileNumberPattern.matcher (mobile);
        if (!matcher.find()) {
            logger.debug("invalid mobile number {}", mobile);
            return false;
        }
        logger.debug("mobile number is valid :{}", mobile);
        return true;
    }

    public static Map<String, String> getProperties (String extraInfo) {
        Map<String, String> properties = new HashMap<>();
        if (StringUtils.isNotEmpty (extraInfo)) {
            String value = extraInfo;
            try {
                if (value.charAt(0) == '{' && value.charAt(value.length() - 1) == '}') {
                }
                value = value.substring(1, value.length() - 1);
            } catch (Exception e) {
                value = value.substring(1, value.length() - 1);
                logger.error( "Exception {} occurred", CommonsUtility.exceptionFormatter(e));
            }
            String[] keyValuePairs = value.split(",");
            for (String pair: keyValuePairs) {
                String[] entry = pair.split("=");
                if (entry != null && entry.length == 2) {
                    properties.put(entry[0].trim(), entry[1].trim());
                }
            }
        }
        return properties;
    }

}
