package com.example.for_chour_kotlin.presentations.fragments.web_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.for_chour_kotlin.databinding.FragmentWebViewBinding  // ViewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    // Ключ для аргументов (URL)
    companion object {
        private const val ARG_URL = "url"
        fun newInstance(url: String): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle().apply {
                putString(ARG_URL, url)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = binding.webView
        progressBar = binding.progressBar

        // Настройки WebView
        setupWebView()

        // Получаем URL из аргументов и загружаем
        val url = arguments?.getString(ARG_URL) ?: "https://chelny-dieta.ru/googlenko/list_hour_stAll.php"  // Дефолт, если null
        loadUrl(url)
    }

    private fun setupWebView() {
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        // WebViewClient для обработки загрузки и ошибок
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE  // Показываем прогресс
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE  // Скрываем прогресс
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Ошибка загрузки: ${error?.description}", Toast.LENGTH_SHORT).show()
                // Опционально: Загрузить локальную страницу с ошибкой
                // webView.loadUrl("file:///android_asset/error.html")
            }

            // Для Android 4.4+ (API 21+): Перехват ссылок внутри страницы
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return true
            }
        }
    }

    private fun loadUrl(url: String) {
        if (url.isNotBlank()) {
            webView.loadUrl(url)
        } else {
            Toast.makeText(context, "Неверный URL", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Очистка WebView (важно для избежания leaks)
        webView.stopLoading()
        webView.clearCache(true)
        webView.clearHistory()
        webView.destroy()
        _binding = null
    }
}
