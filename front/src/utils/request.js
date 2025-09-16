import axios from 'axios'
import ElementUI from "element-ui";
// import "@/utils/initialize"
import router from '../router'

const request = axios.create({
    baseURL: 'http://localhost:9191',  // 注意！！ 这里是全局统一加上了 '/api' 前缀，也就是说所有接口都会加上'/api'前缀在，页面里面写接口的时候就不要加 '/api'了，否则会出现2个'/api'，类似 '/api/api/user'这样的报错，切记！！！
    timeout: 100000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;CHARSET=utf-8';
    let user = JSON.parse(localStorage.getItem("user"))
    if (user) {
        config.headers['token'] = user.token;  // 设置token
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据（安全解析）
        if (typeof res === 'string') {
            try {
                res = res ? JSON.parse(res) : res
            } catch (parseError) {
                // 如果解析失败，保留原始字符串
            }
        }
        // 发生错误，如token失效，则返回登录
        if (res && res.code === '402') {
            ElementUI.MessageBox({
                title: '错误',
                message: res.msg
            }).then(() => {
                router.push('/login')
            })
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        // 统一处理 HTTP 错误，返回一个可在 .then 中处理的对象，避免未捕获的 Promise rejection
        let msg = '';
        if (error.response && error.response.data) {
            msg = error.response.data;
        } else if (error.message) {
            msg = error.message;
        } else {
            msg = '请求失败';
        }
        // 使用与后端相同的响应结构，便于前端现有逻辑处理
        const unifiedError = { code: String(error.response ? error.response.status : 500), msg: msg };
        // 同时展示提示
        ElementUI.Message({ showClose: true, message: unifiedError.msg, type: 'error', duration: 5000 });
        // 返回 resolved 的错误对象，这样调用方即使没有 .catch 也不会抛出未捕获异常
        return Promise.resolve(unifiedError);
    }
)


export default request

