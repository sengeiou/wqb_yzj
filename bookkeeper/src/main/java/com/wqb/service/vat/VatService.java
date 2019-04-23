package com.wqb.service.vat;

import com.wqb.common.BusinessException;
import com.wqb.model.*;
import com.wqb.model.vomodel.KcCommodityVo;
import com.wqb.model.vomodel.PageSub;
import com.wqb.model.vomodel.RedisSub;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

//增值税结转
public interface VatService {

    // 初始化1
    void subinit(Map<String, Object> param);

    public void subinit(User user, Account account);

    // 初始化2
    void subinit(String accountID, String period, String userId, String userName);

    public Map<String, Object> zzsCarryover(Map<String, Object> param) throws BusinessException;

    public Map<String, Object> fjsCarryover(Map<String, Object> param) throws BusinessException;

    // 查询凭证体
    public List<VoucherBody> queryVoBody(Map<String, Object> map) throws BusinessException;

    // 查询凭证头
    public VoucherHead queryVoHeahder(Map<String, Object> map) throws BusinessException;

    public Map<String, Object> test1() throws BusinessException;

    // 创建科目对象 （参数：科目编码，上级编码，科目名字，科目全名）
    public SubjectMessage createSub(String subCode, String parentCode, String subName, String fullName)
            throws BusinessException;

    public SubjectMessage createSub(String subCode, String parentCode, String subName, String fullName,
                                    String subSource, String unit, String number) throws BusinessException;

    public SubjectMessage createNewSub(String parentCode, String subName, String fullName, String period, String aid, String flg) throws BusinessException;

    // 科目查询（参数：科目名字，上级编码，科目编码长度）
    public SubjectMessage querySub(String subName, String code, String len) throws BusinessException;

    //// 计算新的科目编码 (上级编码 科目编码长度 科目名称默认值)
    public String getNumber(String superCode, String len, String num) throws BusinessException;

    // 更新科目到数据库
    //public void updateSubject(SubjectMessage sub) throws BusinessException;

    // 更新科目到数据库
    public void updateSubjest(SubjectMessage sub, Map<String, Object> map) throws BusinessException;

    // 创建子凭证（主凭证id，分录号，摘要，科目名称，借方金额，贷方金额，方向，科目编码,计量单位）
    public VoucherBody createVouchBody(String vouchID, String rowIndex, String vcabstact, String vcsubject,
                                       Double debitAmount, Double creditAmount, String direction, String subCode, String vcunit)
            throws BusinessException;

    // 创建主凭证
    public VoucherHead createVoucherHead(Integer source, String des, Double credit, Double dbit)
            throws BusinessException;

    // 计提结转
    Map<String, Object> jtCarryover(Map<String, Object> param) throws BusinessException;

    //public Voucher delEmptyObject(Voucher vo);

    // 成本结转
    public Map<String, Object> cbCarryover(Map<String, Object> param) throws BusinessException;

    // 进销导入添加到库存表
	/*public void updateKcCommodity(Map<String, Object> param, Map<String, InvoiceBody> map, String type)
			throws BusinessException;*/

    // 库存商品批量添加
    //public void insertKcComm(Map<String, InvoiceBody> map) throws BusinessException;

    // 库存商品添加
    //public void insertKcComm(InvoiceBody invoiceBody) throws BusinessException;
    // 创建和更新科目
    // public SubjectMessage createKuChuSub(Map<String, Object> map) throws
    // BusinessException;

    public SubjectMessage querySubByCode(String subcode) throws BusinessException;


    SubjectMessage querySub(String subName, String code, String len, String flag) throws BusinessException;

    //检查凭证是否需要修正
    public boolean checkVouch(Map<String, Object> param, Voucher voucher) throws BusinessException;

    //public void updateKcCommodity21(Map<String, InvoiceBody> param) throws BusinessException;

    //public void updateKcCommodity31(Map<String, InvoiceBody> param) throws BusinessException;
    //public Map<String, Object> modiyjxpz(Voucher voucher) throws BusinessException;
    //更新科目金额
    public void upSubAmount(Double amount, String direction, String sub_code) throws BusinessException;

    public void modifyAcc() throws BusinessException;

    public void toNextKcCommPeriod(Map<String, Object> param) throws BusinessException;

    public Map<String, Object> addVbQueryCbjz(Voucher voucher) throws BusinessException;

    //void insertStateTrack(Integer type)  throws BusinessException;

    public void upSub(List<Voucher> vo, Map<String, Object> param) throws BusinessException;

    public void insertVouchBatch(VoucherBody... vbs) throws BusinessException;

    public void insertVouchBatch(List<VoucherBody> list) throws BusinessException;

    //public SubjectMessage querySubTwo(String subName, String supperCode,String flg,String type) throws BusinessException;

    public KcCommodity queryQmNum(Map<String, Object> param) throws BusinessException;

    public Map<String, String> getAllMappingSubCode(String accountID) throws BusinessException;

    public Map<String, SubjectMessage> getSubMessageByCode(String accID, Map<String, String> mappingSub, String[] arr) throws BusinessException;
    //public SubjectMessage getSub6001() throws BusinessException;

    int xxCommodityStorage(String accountID, String period) throws BusinessException;

    List<RedisSub> queryRedisSub(Map<String, Object> map) throws BusinessException;

    List<RedisSub> queryRedisSub(String accountID, String period) throws BusinessException;

    public String getKey(String accountID, String period);

    public String getOneSubId(String accountID, String period, String code) throws BusinessException;

    public Map<String, String> getAllSubId(String accountID, String period) throws BusinessException;

    public String resetCache(Map<String, Object> param) throws BusinessException;

    public String resetCache(HttpSession session) throws BusinessException;

    public SubjectMessage querySubjectMessageById(String id) throws BusinessException;

    public TBasicSubjectMessage queryTBasicSubjectMessageById(String id) throws BusinessException;

    public RedisSub getOneSub(String accountID, String period, String code) throws BusinessException;

    public List<RedisSub> getAllSub(String accountID, String period) throws BusinessException;

    public String setCahceSub(String accountID, String period, List<RedisSub> list);

    public String addSubToCache(String accountID, String period, RedisSub sub) throws BusinessException;

    public String resetCache(String accountID, String period) throws BusinessException;

    public List<RedisSub> querySubToPage(Map<String, Object> param, List<VoucherBody> list) throws BusinessException;

    public List<RedisSub> querySubToPage(String accountID, String period, List<VoucherBody> list) throws BusinessException;

    public Integer getMaxVoucherNo() throws BusinessException;

    public Integer getMaxVoucherNo(String accountID, String period) throws BusinessException;

    List<PageSub> queryAllSubToPage(String accountID, String period, String type) throws BusinessException;

    public Long delCache(String accountID, String period) throws BusinessException;

    Map<String, Object> addSubMessage(String accountID, String period, String subCode, String subName, String fullName) throws BusinessException;

    public boolean getChg();

    public void setChg(boolean bool);

    boolean removeCacheSub(String accountID, String period, String subCode) throws BusinessException;

    public String resetCache() throws BusinessException;

    public void errResetCache(Account acc, String busDate);

    public String getSubJson(String accountID, String period);

    public List<PageSub> getPageSubByCodes(String accountID, String period, List<String> codes) throws BusinessException;

    Map<String, Object> queryNextOrPrevious(String accountID, String period, String voucherNo, String type, String type2) throws BusinessException;

    Map<String, Object> goinAddVoucherPage(Account account, String period, User user) throws BusinessException;

    List<KcCommodityVo> queryCommodityToVoucher(Account account, String period) throws BusinessException;

    Voucher commodityGenerateVoucher(String accountID, String period, String comids) throws BusinessException;

    public Map<String, Object> smallZzsCarryover(Map<String, Object> param) throws BusinessException;

    List<InvoiceBody> sumInvoiceBody(List<InvoiceBody> vbList);
}
