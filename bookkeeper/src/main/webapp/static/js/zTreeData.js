/**
 * 2018/1/31
 * 树杈型数据展示组件
 * @param {*} options 对象
 */
(function (w) {
    function TreeData(options) {
        this.$JQ = options.$JQ; // jq对象
        this.initData = options.initData;// 插件初始化
        this.ulElem = options.ulElem; // 需要的插件id
        this.Intercept = options.Intercept; // 截留对象的ID
        this.subUlr = options.subUrl || window.baseURL + '/subject/querySubMessageByCategory?category='; // 科目列表接口
        this.category = options.category || 1; // 当前的科目列表
        this.codeName = options.codeName || ''; // 搜索关键字
        TreeData.flag = options.flag; // 点击截留
        this.init(); // 初始化
        this.arrayData = []; // 整合json 数据
    }
    TreeData.prototype = {
        init: function () {
            //
            this.getAjax(this.category, this.codeName, this.getTreeData);
        },
        // 获取各级科目
        getTreeData: function (list) {
            var that = this;
            var newArr = [],
                obj = {};

            //首先把不同级的数据分类，并添加childs属性，方便子集的插入
            list.forEach(function (item) {
                if (item.subCode) {
                    //以级别为key定义对象数组,这里级别一般以数字为主方便在下面插入数组的时候自动排序
                    if (!obj[item.codeLevel]) {
                        obj[item.codeLevel] = [];
                    }
                    //如果该级别已存在则直接添加到该级别数组
                    obj[item.codeLevel].push(
                        {
                            'id': item.subCode,
                            'pId': item.codeLevel,
                            'name': item.subCode + ' - ' + item.subName,
                            'subCode': item.subCode,
                            'superiorCoding': item.superiorCoding
                        }
                    );
                }
            });

            //把数据插入数组
            for (var item in obj) {
                newArr.push(obj[item]);
            }

            //倒序插入父级
            var len = newArr.length - 1;
            that.arrayData = newArr[0];
            if (len === 0) { // 只有一个数组的时候，说明只有一级
                // 初始化zTree
                that.$JQ.init(that.ulElem, that.initData, newArr[0]);
                return false;
            } else if (len >= 1) {
                //由于是倒序所以使用while循环，避免for循环每次的比较
                while (len) {
                    that.getSubLevel(newArr[len], newArr[len - 1]);
                    len--;
                }
            } else {
                that.ulElem.html('<li style="line-height: 60px;font-size:16px;text-align: center;">暂无数据</li>');
                return false;
            }
        },
        // 整理树杈型json数据
        getSubLevel: function (child, parent) {
            var that = this;
            //在这里先循环子集，在与对应的父级匹配之后break;减少父级循环
            for (var a = 0; a < child.length; a++) {
                for (var i = 0; i < parent.length; i++) {
                    if (child[a].superiorCoding === parent[i].subCode) {
                        child[a].pId = parent[i].id; //父元素的id 和 子元素的pId 相等
                        that.arrayData.push(child[a])
                        break;
                    }
                }
            }

            //整合数据
            var zTreeArray = that.arrayData;

            //如果没有值就提示用户
            if (parent.length <= 0) {
                that.ulElem.html('<li style="line-height: 60px;font-size:16px;text-align: center;">暂无数据</li>');
                return false;
            }
            //初始化zTree
            that.$JQ.init(that.ulElem, that.initData, zTreeArray);

        },
        //获取科目列表
        getAjax: function (category, code, cb) {
            var that = this;
            //如果有就不再重复
            if (TreeData.flag) {
                return false;
            }
            layer.load();
            $.ajax({
                'type': "GET",
                'url': this.subUlr + category + '&subject=' + code,
                'data': {},
                'dataType': 'json',
                success: function (res) {
                    layer.closeAll('loading');
                    if (res.code === 1) {
                        if (that.Intercept) {
                            that.Intercept.attr('data-tab', 'isTab').siblings().removeAttr('data-tab');
                        }
                        cb && cb.call(that, res.subMessageList);
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layer.msg(err);
                }
            })
        }
    };
    window.TreeData = TreeData;
}(window))
